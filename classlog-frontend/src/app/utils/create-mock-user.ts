import {UserDto} from "../model/entities/user-dto";
import {Role} from "../model/entities/role";

export function createMockUserDto(
  id: number,
  email: string,
  roleName: string
): UserDto {
  const role: Role = {
    id,
    roleName,
  };

  return {
    id,
    name: `TestName${id}`,
    surname: `TestSurname${id}`,
    email,
    role,
    createdAt: new Date(),
    token: `mock-token-${id}`,
  };
}
