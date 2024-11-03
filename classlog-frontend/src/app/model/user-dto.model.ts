import { Role } from './role.model';

export interface UserDto {
  id: number;
  name: string;
  surname: string;
  email: string;
  role: Role;  // Use the Role interface here
  createdAt: Date;
  token: string;
}
