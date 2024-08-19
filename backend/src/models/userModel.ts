export interface User {
  uid: string;
  email: string;
  role: "super-admin" | "space-admin" | "regular-user";
  assignedSpaces: string[];
}
