import { Role } from './role.model';

export interface UserDto {
  id: number;
  firstName: string;
  lastName: string;
  login: string;
  role: Role;  // Use the Role interface here
  token: string;
}
