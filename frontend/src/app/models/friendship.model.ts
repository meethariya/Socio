import { User } from './user.model';

export enum FriendshipStatus {
  PENDING = "PENDING",
  ACCEPTED = "ACCEPTED",
  REJECTED = "REJECTED",
}
export type Friendship = {
  id: number;

  sender: User;

  receiver: User;

  status: FriendshipStatus;

  timestamp: Date;
};
