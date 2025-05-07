export enum MessageStatus {
  NOT_SENT = "NOT_SENT",
  SENT = "SENT",
  READ = "READ",
}
export type Message = {
  id: string;
  senderId: number;
  receiverId: number;
  content: string;
  status: MessageStatus;
  timestamp: Date;
};
