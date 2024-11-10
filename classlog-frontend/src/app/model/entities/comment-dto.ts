import {PostDto} from "./post-dto";
import {UserDto} from "./user-dto";


export interface CommentDto {
  id: number;
  post: PostDto;
  user: UserDto;
  content: string;
  createdAt: Date;
}
