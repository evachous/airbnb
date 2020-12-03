import {Accommodation, Address} from "./accommodation";

export class User {
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  city: string;
  country: string;
  isAdmin: boolean;
  isHost: boolean;
  isGuest: boolean;
  isApproved: boolean;
  accommodations: any;
  profilePicture: any;
  rating: number;
  numRatings: number;
  avgRating: number;
}

export class SearchHistory {
  id: number;
  guest: User;
  accommodations: Accommodation[];
  addresses: Address[];
}
