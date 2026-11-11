// no side effects allowed due to re-export by index.ts

import type { Status } from './status';

export interface GameData {
  game: Game;
  player: Player;
  opponent: Player;
  takebackable: boolean;
  moretimeable: boolean;
  clock?: Clock;
  correspondence?: CorrespondenceClock;
}

export interface Game {
  id: string;
  status: Status;
  player: Color;
  turns: number;
  fen: FEN;
  startedAtTurn?: number;
  source: Source;
  speed: Speed;
  variant: Variant;
  winner?: Color;
  abortedBy?: Color;
  drawOffers?: number[];
  moveCentis?: number[];
  initialFen?: string;
  threefold?: boolean;
  fiftyMoves?: boolean;
  boosted?: boolean;
  rematch?: string;
  rated?: boolean;
  perf: VariantKey | Speed;
  rules?: GameRule[];
}

export declare type GameRule = 'noAbort' | 'noRematch' | 'noGiveTime' | 'noClaimWin';

export type TopOrBottom = 'top' | 'bottom';

export interface Player {
  id: string;
  name: string | null;
  user?: PlayerUser;
  spectator?: boolean;
  color: Color;
  proposingTakeback?: boolean;
  offeringRematch?: boolean;
  offeringDraw?: boolean;
  ai?: number;
  onGame: boolean;
  lastDrawOfferAtPly?: Ply;
  isGone: number | boolean;
  blurs?: Blurs;
  hold?: Hold;
  ratingDiff?: number;
  checks?: number;
  rating?: number;
  provisional?: boolean;
  engine?: boolean;
  berserk?: boolean;
  version: number;
  image?: string;
  blindfold?: boolean;
}

export interface Clock {
  running: boolean;
  initial: number;
  increment: number;
}
export interface CorrespondenceClock {
  daysPerTurn: number;
  increment: number;
  white: number;
  black: number;
}

export type Source = 'import' | 'lobby' | 'pool' | 'friend' | 'ai' | 'local';

export interface PlayerUser {
  id: string;
  online: boolean;
  username: string;
  patron?: boolean;
  patronColor?: PatronColor;
  title?: string;
  flair?: Flair;
  perfs: Partial<Record<VariantKey | Speed, Perf>>;
}

export interface Perf {
  games: number;
  rating: number;
  rd: number;
  prog: number;
  prov?: boolean;
}

export interface Blurs {
  nb: number;
  percent: number;
  bits?: string;
}

export interface Hold {
  ply: number;
  mean: number;
  sd: number;
}
