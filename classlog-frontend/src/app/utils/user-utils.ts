import {UserDto} from "../model/entities/user-dto";

export function getFullName(user: UserDto): string {
  return `${user.name} ${user.surname}`;
}
