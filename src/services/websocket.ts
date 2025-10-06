import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export interface LeaderboardEntry {
  userId: number;
  name: string;
  avatar: string;
  totalSteps: number;
  rank: number;
  totalDistance?: number;
  territoriesClaimed?: number;
}

class WebSocketService {
  private stompClient: any = null;
  private isConnected = false;

  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      const wsUrl = process.env.REACT_APP_WS_URL || 'ws://localhost:8080/ws';
      const socket = new SockJS(wsUrl);
      this.stompClient = Stomp.over(socket);
      
      this.stompClient.connect({}, () => {
        this.isConnected = true;
        console.log('WebSocket connected');
        resolve();
      }, (error: any) => {
        console.error('WebSocket connection error:', error);
        reject(error);
      });
    });
  }

  disconnect() {
    if (this.stompClient && this.isConnected) {
      this.stompClient.disconnect();
      this.isConnected = false;
      console.log('WebSocket disconnected');
    }
  }

  subscribeToDailyLeaderboard(callback: (data: LeaderboardEntry[]) => void) {
    if (this.stompClient && this.isConnected) {
      this.stompClient.subscribe('/topic/leaderboard/daily', (message: any) => {
        const data = JSON.parse(message.body);
        callback(data);
      });
    }
  }

  subscribeToWeeklyLeaderboard(callback: (data: LeaderboardEntry[]) => void) {
    if (this.stompClient && this.isConnected) {
      this.stompClient.subscribe('/topic/leaderboard/weekly', (message: any) => {
        const data = JSON.parse(message.body);
        callback(data);
      });
    }
  }

  subscribeToAllTimeLeaderboard(callback: (data: LeaderboardEntry[]) => void) {
    if (this.stompClient && this.isConnected) {
      this.stompClient.subscribe('/topic/leaderboard/all-time', (message: any) => {
        const data = JSON.parse(message.body);
        callback(data);
      });
    }
  }

  requestDailyLeaderboard() {
    if (this.stompClient && this.isConnected) {
      this.stompClient.send('/app/leaderboard/daily', {}, '');
    }
  }

  requestWeeklyLeaderboard() {
    if (this.stompClient && this.isConnected) {
      this.stompClient.send('/app/leaderboard/weekly', {}, '');
    }
  }

  requestAllTimeLeaderboard() {
    if (this.stompClient && this.isConnected) {
      this.stompClient.send('/app/leaderboard/all-time', {}, '');
    }
  }
}

export const websocketService = new WebSocketService();

