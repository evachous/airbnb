export class Accommodation {
  info: AccommodationInfo;
  location: AccommodationLocation;
  rules: AccommodationRules;
}

export class AccommodationInfo {
  type: string;
  beds: number;
  bedrooms: number;
  bathrooms: number;
  livingRoom: boolean;
  area: number;
  description: string;
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
}
