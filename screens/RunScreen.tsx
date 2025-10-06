import React, { useState, useEffect, useRef } from 'react';
import type { Screen } from '../types';
import { PauseIcon, PlayIcon } from '../components/icons';
import { apiService, Run } from '../src/services/api';

// Since we are using Leaflet via CDN, we declare the 'L' global variable.
declare const L: any;

interface RunScreenProps {
  onNavigate: (screen: Screen) => void;
  user: any;
}

const RunScreen: React.FC<RunScreenProps> = ({ onNavigate, user }) => {
  const [isPaused, setIsPaused] = useState(false);
  const [seconds, setSeconds] = useState(0);
  const [path, setPath] = useState<[number, number][]>([]);
  const [locationError, setLocationError] = useState<string | null>(null);
  const [currentRun, setCurrentRun] = useState<Run | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [otherTerritories, setOtherTerritories] = useState<any[]>([]);
  
  const intervalRef = useRef<number | null>(null);
  const watchIdRef = useRef<number | null>(null);

  // Refs for Leaflet map objects
  const mapRef = useRef<any>(null);
  const polylineRef = useRef<any>(null);
  const markerRef = useRef<any>(null);
  const mapContainerRef = useRef<HTMLDivElement>(null);

  const isGuest = !user || !user.id || user.id === 0;

  // Timer effect (runs locally irrespective of backend latency)
  useEffect(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }
    if (!isPaused) {
      intervalRef.current = window.setInterval(() => {
        setSeconds(prev => prev + 1);
      }, 1000);
    }
    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    };
  }, [isPaused]);

  // Start run effect
  useEffect(() => {
    const startRun = async () => {
      if (user && !currentRun) {
        try {
          setIsLoading(true);
          const run = await apiService.startRun(user.id);
          setCurrentRun(run);
        } catch (error) {
          console.error('Error starting run:', error);
          setLocationError('Failed to start run. Please try again.');
        } finally {
          setIsLoading(false);
        }
      }
    };

    startRun();
  }, [user, currentRun]);

  // Map and location tracking effect
  useEffect(() => {
    const startWatcher = () => {
      if (isPaused || !currentRun) {
        if (watchIdRef.current) navigator.geolocation.clearWatch(watchIdRef.current);
        watchIdRef.current = null;
        return;
      }
      
      if (navigator.geolocation && !watchIdRef.current) {
         watchIdRef.current = navigator.geolocation.watchPosition(
            async (position) => {
              const { latitude, longitude } = position.coords;
              const newPosition: [number, number] = [latitude, longitude];
              
              setPath(prevPath => [...prevPath, newPosition]);

              if (mapRef.current) {
                mapRef.current.setView(newPosition, 16);
                if (markerRef.current) {
                  markerRef.current.setLatLng(newPosition);
                }
              }

              // Send location to backend
              if (currentRun) {
                try {
                  await apiService.addRunPoint(currentRun.id, latitude, longitude);
                } catch (error) {
                  console.error('Error sending location:', error);
                }
              }
            },
            (error) => {
               console.error("Geolocation watch error:", error);
               // Minor errors during a run might not need a full screen takeover
            },
            { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
          );
      }
    };

    // Initialize map (show campus by default; update on GPS)
    if (mapContainerRef.current && !mapRef.current) {
      if (!navigator.geolocation) {
        setLocationError("Geolocation is not supported by your browser.");
        return;
      }

      // Initialize map at SRM KTR center while waiting for GPS
      const SRM_CENTER: [number, number] = [12.8232, 80.0452];
      const SRM_BOUNDS = L.latLngBounds([12.8195, 80.0400], [12.8275, 80.0490]);
      const map = L.map(mapContainerRef.current).setView(SRM_CENTER, 16);
      map.setMaxBounds(SRM_BOUNDS);
      map.on('drag', () => {
        map.panInsideBounds(SRM_BOUNDS, { animate: false });
      });
      L.tileLayer('https://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
          maxZoom: 20,
          subdomains:['mt0','mt1','mt2','mt3'],
          attribution: '&copy; <a href="https://www.google.com/maps">Google Maps</a>'
      }).addTo(map);
      polylineRef.current = L.polyline([], { color: '#EF4444' }).addTo(map);
      const userIcon = L.divIcon({
          className: 'custom-user-marker',
          html: '<div class="w-4 h-4 bg-red-500 rounded-full border-2 border-white shadow-md"></div>',
          iconSize: [16, 16],
          iconAnchor: [8, 8]
      });
      markerRef.current = L.marker(SRM_CENTER, { icon: userIcon }).addTo(map);
      mapRef.current = map;

      // Then ask for precise position
      navigator.geolocation.getCurrentPosition(
        (initialPosition) => {
          setLocationError(null);
          const { latitude, longitude } = initialPosition.coords;
          mapRef.current.setView([latitude, longitude], 16);
          markerRef.current.setLatLng([latitude, longitude]);
          setPath([[latitude, longitude]]); // Start path at current location
          startWatcher();
        },
        (error) => {
          if (error.code === error.PERMISSION_DENIED) {
            setLocationError("Location access denied. Please enable location permissions in your browser settings to start your run.");
          } else {
             setLocationError("Could not determine your location. Please ensure location services are enabled and try again.");
          }
        },
        { enableHighAccuracy: true, timeout: 10000, maximumAge: 60000 }
      );
    } else {
       // If map is already initialized, just manage the watcher
       startWatcher();
    }

    // Cleanup function
    return () => {
      if (watchIdRef.current) {
        navigator.geolocation.clearWatch(watchIdRef.current);
      }
    };
  }, [isPaused, currentRun]);

  // Effect to update the polyline when path changes
  useEffect(() => {
    if (polylineRef.current) {
      polylineRef.current.setLatLngs(path);
    }
  }, [path]);

  // Load other active territories to display on map
  useEffect(() => {
    const loadTerritories = async () => {
      try {
        const res = await fetch(`${process.env.REACT_APP_API_URL || 'http://localhost:8080/api'}/territories/active`);
        const data = await res.json();
        setOtherTerritories(data);
        if (mapRef.current && Array.isArray(data)) {
          data.forEach((t: any) => {
            if (t.polygon && t.polygon.coordinates) {
              // Expecting GeoJSON-like array [ [ [lon,lat], ... ] ]
              const ring = t.polygon.coordinates[0].map((c: number[]) => [c[1], c[0]]);
              L.polygon(ring, { color: '#22c55e', fillColor: '#22c55e', fillOpacity: 0.2 }).addTo(mapRef.current);
            }
          });
        }
      } catch (e) {
        // ignore for MVP
      }
    };
    loadTerritories();
  }, []);

  const handleEndRun = async () => {
    if (isGuest || !currentRun) {
      setIsPaused(true);
      if (mapRef.current && path.length > 2) {
        L.polygon([...path, path[0]], { color: '#EF4444', fillColor: '#EF4444', fillOpacity: 0.3 }).addTo(mapRef.current);
      }
      onNavigate('home');
      return;
    }
    if (currentRun) {
      try {
        setIsLoading(true);
        setIsPaused(true); // stop timer immediately
        await apiService.endRun(currentRun.id);
        
        // Draw the final claimed territory polygon
        if (mapRef.current && path.length > 2) {
          L.polygon(path, { color: '#EF4444', fillColor: '#EF4444', fillOpacity: 0.3 }).addTo(mapRef.current);
        }
        
        onNavigate('home');
      } catch (error) {
        console.error('Error ending run:', error);
        setLocationError('Failed to end run. Please try again.');
      } finally {
        setIsLoading(false);
      }
    }
  };

  const handlePauseResume = async () => {
    if (isGuest || !currentRun) {
      setIsPaused(p => !p);
      return;
    }

    try {
      // Optimistic local toggle so timer/UI respond instantly
      const next = !isPaused;
      setIsPaused(next);
      if (next === false) {
        await apiService.pauseRun(currentRun.id);
      } else {
        await apiService.resumeRun(currentRun.id);
      }
    } catch (error) {
      console.error('Error pausing/resuming run:', error);
      // revert if API fails
      setIsPaused(prev => !prev);
    }
  };

  const formatTime = (totalSeconds: number) => {
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const secs = totalSeconds % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="flex flex-col h-full text-white bg-[#0F172A]">
      <style>{`.leaflet-control-attribution, .leaflet-control-zoom { display: none !important; }`}</style>
      
      <header className="flex-shrink-0 flex justify-center items-center p-6 pt-12 bg-slate-900/50 backdrop-blur-sm z-10">
        <h1 className="text-xl font-bold">Run in Progress</h1>
      </header>

      <main className="flex-grow relative">
        <div className="absolute inset-0" ref={mapContainerRef}>
          {isLoading && (
            <div className="w-full h-full bg-slate-800 flex flex-col items-center justify-center p-4 text-center">
              <h3 className="text-xl font-semibold text-yellow-400 mb-2">Starting Run...</h3>
              <p className="text-gray-300">Please wait while we set up your run.</p>
            </div>
          )}
          {locationError && (
            <div className="w-full h-full bg-slate-800 flex flex-col items-center justify-center p-4 text-center">
              <h3 className="text-xl font-semibold text-yellow-400 mb-2">Location Required</h3>
              <p className="text-gray-300">{locationError}</p>
            </div>
          )}
        </div>
      </main>
      
      <footer className="flex-shrink-0 p-6 flex flex-col items-center bg-transparent z-10">
          <div className="bg-slate-900/80 backdrop-blur-sm p-4 rounded-2xl w-full max-w-sm mb-6 text-center shadow-lg">
            <p className="text-gray-400 text-lg">TIME</p>
            <p className="text-6xl font-bold tabular-nums">{formatTime(seconds)}</p>
          </div>

          <div className="flex items-center justify-center">
            {isPaused ? (
              <button
                onClick={handlePauseResume}
                disabled={isLoading}
                className="w-20 h-20 bg-green-500 rounded-full flex items-center justify-center text-white shadow-lg hover:bg-green-600 transition transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
                aria-label="Resume Run"
              >
                <PlayIcon className="w-10 h-10" />
              </button>
            ) : (
              <button
                onClick={handlePauseResume}
                disabled={isLoading}
                className="w-20 h-20 bg-yellow-500 rounded-full flex items-center justify-center text-white shadow-lg hover:bg-yellow-600 transition transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
                aria-label="Pause Run"
              >
                <PauseIcon className="w-10 h-10" />
              </button>
            )}
          </div>
          <button 
            onClick={handleEndRun}
            className="mt-6 px-6 py-2 text-red-400 font-semibold hover:text-red-300 transition"
          >
            End Run
          </button>
      </footer>
    </div>
  );
};

export default RunScreen;
