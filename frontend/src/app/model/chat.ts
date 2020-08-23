import {Accommodation} from "./accommodation";

export class Chat {
  id: number;
  accommodation: Accommodation;
  guestUsername: string;
  messages: ChatMessage[];
}

export class ChatMessage {
  senderUsername: string;
  message: string;
  timestamp: string;
}
