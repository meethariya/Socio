export type User = {
  id: number;
  username: string;
  email: string;
  name: string;
  authId: number;
  isFriend?:'NOT FRIEND'|'REQUEST SENT'|'REQUEST RECEIVED'|'FRIENDS';
};
