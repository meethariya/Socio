import { User } from './user.model';

export enum FriendshipStatus {
  PENDING,
  ACCEPTED,
  REJECTED,
}
export type Friendship = {
  id: number;

  sender: User;

  receiver: User;

  status: FriendshipStatus;

  timestamp: Date;
};
