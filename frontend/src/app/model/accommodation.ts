export class Accommodation {
  id: number;
  info: AccommodationInfo;
  location: AccommodationLocation;
  rules: AccommodationRules;
}

export class AccommodationInfo {
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
  address: string;
  neighborhood: string;
  transportation: string;
}

export class AccommodationRules {
  smoking: boolean;
  pets: boolean;
  events: boolean;
  minDays: number;
  maxPeople: number;
}
