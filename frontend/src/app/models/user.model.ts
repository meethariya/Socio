export type User = {
  id: number;
  username: string;
  email: string;
  name: string;
  authId: number;
  profilePic: string | null;
  isFriend?:'NOT FRIEND'|'REQUEST SENT'|'REQUEST RECEIVED'|'FRIENDS';
};
