export type Post = {
  id: string;
  caption: string;
  userId: number;
  imageUrl: string;
  location: string;
  covered: boolean;
  needBlurBackground: boolean;
  timestamp: Date;
  likeCount: number;
  likedByVisitor?:boolean;
};
