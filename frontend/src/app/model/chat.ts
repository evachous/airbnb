import {Accommodation} from "./accommodation";

export class Chat {
  id: number;
  accommodation: Accommodation;
  guestUsername: string;
  history: ChatHistory[];
}

export class ChatHistory {
  senderUsername: string;
  message: string;
  timestamp: string;
}
