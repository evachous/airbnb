import {Accommodation} from "./accommodation";
import {User} from "./user";

export class Chat {
  id: number;
  accommodation: Accommodation;
  guest: User;
  messages: ChatMessage[];
}

export class ChatMessage {
  senderUsername: string;
  message: string;
  timestamp: string;
}
