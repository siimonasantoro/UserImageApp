import { Image } from "./image.model";
import { Role, User } from "./user.model";

export interface ResponseDTO {
  status: boolean;
    message: string;
    error?: string;
    content: {
        token: string;
        user: User; 
        userId: string; 
        roles?: Role[];
        images?: Image[];
    };
}