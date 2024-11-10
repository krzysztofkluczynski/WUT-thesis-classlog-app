import { Role } from './role';

export interface UserDto {
  id: number;
  name: string;
  surname: string;
  email: string;
  role: Role;
  createdAt: Date;
  token: string;
}
