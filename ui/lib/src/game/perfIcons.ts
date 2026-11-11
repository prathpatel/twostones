import { licon, type LiconValue } from '@/licon';

const perfIcons: Record<VariantKey | Speed, LiconValue> = {
  ultraBullet: licon.UltraBullet,
  bullet: licon.Bullet,
  blitz: licon.FlameBlitz,
  rapid: licon.Rabbit,
  classical: licon.Turtle,
  correspondence: licon.PaperAirplane,
  standard: licon.CrownElite,
};

export default perfIcons;
