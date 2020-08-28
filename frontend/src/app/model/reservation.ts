import {Accommodation} from "./accommodation";
import {User} from "./user";

export class Reservation {
  id: number;
  guest: User;
  accommodation: Accommodation;
  numPeople: number;
  startDate: any;
  endDate: any;
  days: number;
  review: Review;
}

export class Review {
  id: number;
  reservation: Reservation;
  text: string;
  rating: number;
}
