import {User} from "./user";
import {Chat} from "./chat";

export class Accommodation {
  id: number;
  info: AccommodationInfo;
  location: AccommodationLocation;
  rules: AccommodationRules;
  host: User;
  images: any;
  //chats: Chat[];
  numRatings: number;
  avgRating: number;
}

export class Address {
  label: string;
  number: string;
  road: string;
  suburb: string;
  city: string;
  state: string;
  postcode: string;
  country: string;
  lat: number;
  lng: number;
}

export class AccommodationInfo {
  name: string;
  startDate: any;
  endDate: any;
  minCost: number;
  costPerPerson: number;
  type: string;
  beds: number;
  bedrooms: number;
  bathrooms: number;
  area: number;
  description: string;
  livingRoom: boolean;
  internet: boolean;
  ac: boolean;
  heating: boolean;
  kitchen: boolean;
  tv: boolean;
  parking: boolean;
  elevator: boolean;
}

export class AccommodationLocation {
  address: Address;
  transportation: string;
}

export class AccommodationRules {
  smoking: boolean;
  pets: boolean;
  events: boolean;
  minDays: number;
  maxPeople: number;
}
