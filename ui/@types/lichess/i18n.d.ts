// Generated
interface I18nFormat {
  (...args: (string | number)[]): string; // formatted
  asArray: <T>(...args: T[]) => (T | string)[]; // vdom
}
interface I18nPlural {
  (quantity: number, ...args: (string | number)[]): string; // pluralSame
  asArray: <T>(quantity: number, ...args: T[]) => (T | string)[]; // vdomPlural / plural
}
interface I18n {
  /** global noarg key lookup */
  (key: string): string;
  quantity: (count: number) => 'zero' | 'one' | 'two' | 'few' | 'many' | 'other';

  activity: {
    /** Activity */
    activity: string;
  };
  arena: {
    /** Arena */
    arena: string;
    /** Berserk */
    berserk: string;
    /** Created */
    created: string;
    /** Arena History */
    history: string;
    /** Start date */
    startDate: string;
    /** Total */
    total: string;
    /** Tournament winners */
    tournamentWinners: string;
    /** Variant */
    variant: string;
  };
  broadcast: {
    /** Age */
    age: string;
    /** Boards */
    boards: string;
    /** Broadcasts */
    broadcasts: string;
    /** Federation */
    federation: string;
    /** Matches */
    matches: string;
    /** My broadcasts */
    myBroadcasts: string;
    /** %s viewers */
    nbViewers: I18nPlural;
    /** Ongoing */
    ongoing: string;
    /** Overview */
    overview: string;
    /** Rating diff */
    ratingDiff: string;
    /** Score */
    score: string;
    /** Teams */
    teams: string;
    /** The following term(s) will be automatically translated: %s. */
    termsAutomaticallyTranslated: I18nFormat;
    /** Time zone */
    timezone: string;
    /** Top 10 rating */
    top10Rating: string;
    /** Upcoming */
    upcoming: string;
    /** Upload tournament image */
    uploadImage: string;
  };
  challenge: {
    /** Cannot challenge due to provisional %s rating. */
    cannotChallengeDueToProvisionalXRating: I18nFormat;
    /** Challenge accepted! */
    challengeAccepted: string;
    /** Challenge cancelled. */
    challengeCanceled: string;
    /** Challenge declined. */
    challengeDeclined: string;
    /** Challenges: %1$s */
    challengesX: I18nFormat;
    /** Challenge */
    challengeToPlay: string;
    /** Please send me a casual challenge instead. */
    declineCasual: string;
    /** I'm not accepting challenges at the moment. */
    declineGeneric: string;
    /** This is not the right time for me, please ask again later. */
    declineLater: string;
    /** I'm not accepting challenges from bots. */
    declineNoBot: string;
    /** I'm only accepting challenges from bots. */
    declineOnlyBot: string;
    /** Please send me a rated challenge instead. */
    declineRated: string;
    /** I'm not accepting variant challenges right now. */
    declineStandard: string;
    /** I'm not accepting challenges with this time control. */
    declineTimeControl: string;
    /** This time control is too fast for me, please challenge again with a slower game. */
    declineTooFast: string;
    /** This time control is too slow for me, please challenge again with a faster game. */
    declineTooSlow: string;
    /** I'm not willing to play this variant right now. */
    declineVariant: string;
    /** Or invite a registered player: */
    inviteLichessUser: string;
    /** Please register to send challenges to this user. */
    registerToSendChallenges: string;
    /** %s does not accept challenges. */
    xDoesNotAcceptChallenges: I18nFormat;
    /** %s only accepts challenges from friends. */
    xOnlyAcceptsChallengesFromFriends: I18nFormat;
    /** You cannot challenge %s. */
    youCannotChallengeX: I18nFormat;
    /** Your %1$s rating is too far from %2$s. */
    yourXRatingIsTooFarFromY: I18nFormat;
  };
  class: {
    /** Class name */
    className: string;
    /** Declined */
    declined: string;
    /** Features */
    features: string;
    /** Invite */
    invite: string;
    /** Managed */
    managed: string;
    /** N/A */
    na: string;
    /** or */
    orSeparator: string;
    /** Overview */
    overview: string;
    /** Pending */
    pending: string;
    /** Progress */
    progress: string;
    /** Real name */
    realName: string;
    /** Graduate */
    release: string;
    /** Reopen */
    reopen: string;
    /** Reset password */
    resetPassword: string;
    /** Students */
    students: string;
    /** Teachers */
    teachers: string;
    /** Win rate */
    winrate: string;
  };
  coach: {
    /** Accepting students */
    accepting: string;
    /** Languages */
    languages: string;
    /** Location */
    location: string;
    /** Rating */
    rating: string;
  };
  contact: {
    /** You can close your account on this page */
    closeYourAccount: string;
    /** Contact */
    contact: string;
    /** Error page */
    errorPage: string;
  };
  coordinates: {
    /** Coordinates */
    coordinates: string;
  };
  dgt: {
    /** Configure */
    configure: string;
    /** Debug */
    debug: string;
    /** Keywords */
    keywords: string;
  };
  emails: {
    /** To contact us, please use %s. */
    common_contact: I18nFormat;
    /** (Link not working? Try pasting it into your browser!) */
    common_linkNotWorking: string;
    /** This is a service email related to your use of %s. */
    common_note: I18nFormat;
    /** To confirm you have access to this email, please click the link below: */
    emailChange_click: string;
    /** You have requested to change your email address. */
    emailChange_intro: string;
    /** Confirm new email address, %s */
    emailChange_subject: I18nFormat;
    /** Click the link to enable your TwoStones account: */
    emailConfirm_click: string;
    /** Thanks for registering with TwoStones! */
    emailConfirm_intro: string;
    /** If you did not register with TwoStones, you can safely ignore this message. The unconfirmed account and all traces of your email address will be deleted from our system after 48 hours. */
    emailConfirm_justIgnore: string;
    /** Confirm your TwoStones account, %s */
    emailConfirm_subject: I18nFormat;
    /** Log in to TwoStones, %s */
    logInToLichess: I18nFormat;
    /** If you made this request, click the link below. If not, you can ignore this email. */
    passwordReset_clickOrIgnore: string;
    /** We received a request to reset the password for your account. */
    passwordReset_intro: string;
    /** Reset your TwoStones password, %s */
    passwordReset_subject: I18nFormat;
    /** Welcome to TwoStones, %s */
    welcome_subject: I18nFormat;
    /** You have successfully created your account. */
    welcome_text: I18nFormat;
  };
  faq: {
    /** Accounts */
    accounts: string;
    /** configure */
    configure: string;
    /** This is a legal move known as "en passant". The Wikipedia article gives a %1$s. */
    explainingEnPassant: I18nFormat;
    /** FAQ */
    faqAbbreviation: string;
    /** What keyboard shortcuts are there? */
    keyboardShortcuts: string;
    /** Lichess is a combination of live/light/libre and chess. It is pronounced %1$s. */
    lichessCombinationLiveLightLibrePronounced: I18nFormat;
    /** positions */
    positions: string;
  };
  features: {
    /** Studies (shareable and persistent analysis) */
    studies: string;
  };
  lag: {
    /** And now, the long answer! Game lag is composed of two unrelated values (lower is better): */
    andNowTheLongAnswerLagComposedOfTwoValues: string;
    /** Is the server lagging? */
    isLichessLagging: string;
    /** Lag compensation */
    lagCompensation: string;
    /** The server compensates for network lag. This includes sustained lag and occasional lag spikes. Compensation limits and rules are based on time control and already-compensated lag, ensuring the game stays fair for both players. As a result, having a higher network lag than your opponent is not a handicap! */
    lagCompensationExplanation: string;
    /** Server latency */
    lichessServerLatency: string;
    /** Measurements in progress... */
    measurementInProgressThreeDot: string;
    /** Network between the server and you */
    networkBetweenLichessAndYou: string;
    /** The time it takes to send a move from your computer to the server, and get a response back. The ping depends on your distance to the server as well as the quality of your Internet connection. */
    networkBetweenLichessAndYouExplanation: string;
    /** No. And your network is bad. */
    noAndYourNetworkIsBad: string;
    /** No. And your network is good. */
    noAndYourNetworkIsGood: string;
    /** The time it takes to process a move on the server. It's the same for everybody, and only depends on the server's load. The more players there are, the higher the load becomes, but we do our best to keep it low. It rarely exceeds 10ms. */
    serverLatencyAndLoadExplanation: string;
    /** Yes. It will be fixed soon! */
    yesItWillBeFixedSoon: string;
    /** You can find both these values at any time, by clicking your username in the top bar. */
    youCanFindTheseValuesAtAnyTimeByClickingOnYourUsername: string;
  };
  learn: {
    /** Awesome! */
    awesome: string;
    /** by playing! */
    byPlaying: string;
    /** Capture */
    capture: string;
    /** Castling */
    castling: string;
    /** You're under attack! */
    escape: string;
    /** Intermediate */
    intermediate: string;
    /** Learn chess */
    learnChess: string;
    /** Menu */
    menu: string;
    /** Next */
    next: string;
    /** play! */
    play: string;
    /** Practise */
    practice: string;
    /** Protection */
    protection: string;
    /** Puzzles */
    puzzles: string;
    /** Register */
    register: string;
    /** Retry */
    retry: string;
    /** Stalemate */
    stalemate: string;
    /** Videos */
    videos: string;
  };
  localAnalysis: {
    /** Analyse */
    analyse: string;
    /** Local */
    local: string;
    /** Publish */
    publish: string;
    /** Quality */
    quality: string;
    /** Using */
    using: string;
  };
  msg: {
    /** There is a new TwoStones mobile app available! */
    lichobileNewAppAvailable: string;
    /** Please download the new official TwoStones app at %s. */
    lichobileNewAppDownload: I18nFormat;
    /** The old app no longer supports puzzles. Please install the new TwoStones app: %s */
    oldAppNoLongerSupportsPuzzles: I18nFormat;
    /** %s doesn't accept new messages. */
    xDoesntAcceptNewMessages: I18nFormat;
  };
  nvui: {
    /** Actions */
    actions: string;
    /** bishop */
    bishop: string;
    /** Copy %s to clipboard */
    copyToClipboard: I18nFormat;
    /** king */
    king: string;
    /** knight */
    knight: string;
    /** Last move */
    lastMove: string;
    /** Pieces */
    pieces: string;
    /** To promote to anything else than a queen, use equals. For example a-8-equals-n promotes to a knight. */
    promotion: string;
    /** rook */
    rook: string;
  };
  oauthScope: {
    /** You already have played games! */
    alreadyHavePlayedGames: string;
    /** API access tokens */
    apiAccessTokens: string;
    /** API documentation */
    apiDocumentation: string;
    /** Here's a %1$s and the %2$s. */
    apiDocumentationLinks: I18nFormat;
    /** Note for the attention of developers only: */
    attentionOfDevelopers: string;
    /** authorization code flow */
    authorizationCodeFlow: string;
    /** Play games using the Board API */
    boardPlay: string;
    /** Play games using the Bot API */
    botPlay: string;
    /** You can make OAuth requests without going through the %s. */
    canMakeOauthRequests: I18nFormat;
    /** Carefully select what it is allowed to do on your behalf. */
    carefullySelect: string;
    /** Create many games at once for other players */
    challengeBulk: string;
    /** Read incoming challenges */
    challengeRead: string;
    /** Send, accept and reject challenges */
    challengeWrite: string;
    /** Make sure to copy your new personal access token now. You won’t be able to see it again! */
    copyTokenNow: string;
    /** Created %s */
    created: I18nFormat;
    /** The token will grant access to your account. Do NOT share it with anyone! */
    doNotShareIt: string;
    /** Read email address */
    emailRead: string;
    /** View and use your external engines */
    engineRead: string;
    /** Create and update external engines */
    engineWrite: string;
    /** Read followed players */
    followRead: string;
    /** Follow and unfollow other players */
    followWrite: string;
    /** For example: %s */
    forExample: I18nFormat;
    /** generate a personal access token */
    generatePersonalToken: string;
    /** Giving these pre-filled URLs to your users will help them get the right token scopes. */
    givingPrefilledUrls: string;
    /** Guard these tokens carefully! They are like passwords. The advantage to using tokens over putting your password into a script is that tokens can be revoked, and you can generate lots of them. */
    guardTokensCarefully: string;
    /** Instead, %s that you can directly use in API requests. */
    insteadGenerateToken: I18nFormat;
    /** Last used %s */
    lastUsed: I18nFormat;
    /** Manage teams you lead: send direct messages, kick members */
    manageTeamsYouLead: string;
    /** Send private messages to other players */
    msgWrite: string;
    /** New personal API access token */
    newAccessToken: string;
    /** External play */
    oauthCatExternalPlay: string;
    /** Interactions */
    oauthCatInteractions: string;
    /** Play games */
    oauthCatPlayGames: string;
    /** User account */
    oauthCatUserAccount: string;
    /** Personal API access tokens */
    personalAccessTokens: string;
    /** personal token app example */
    personalTokenAppExample: string;
    /** It is possible to pre-fill this form by tweaking the query parameters of the URL. */
    possibleToPrefill: string;
    /** Read preferences */
    preferenceRead: string;
    /** Write preference */
    preferenceWrite: string;
    /** Read puzzle activity */
    puzzleRead: string;
    /** Solve puzzles */
    puzzleWrite: string;
    /** Create and join puzzle races */
    racerWrite: string;
    /** Read and write notes on other players */
    readWriteNotesOnPlayers: string;
    /** So you remember what this token is for */
    rememberTokenUse: string;
    /** Read private studies and broadcasts */
    studyRead: string;
    /** Create, update, delete studies and broadcasts */
    studyWrite: string;
    /** Read private team information */
    teamRead: string;
    /** Join and leave teams */
    teamWrite: string;
    /** ticks the %1$s and %2$s scopes, and sets the token description. */
    ticksTheScopes: I18nFormat;
    /** Token description */
    tokenDescription: string;
    /** A token grants other people permission to use your account. */
    tokenGrantsPermission: string;
    /** Read private tournaments */
    tournamentRead: string;
    /** Create, update, and join tournaments */
    tournamentWrite: string;
    /** Use moderator tools (within bounds of your permission) */
    webMod: string;
    /** What the token can do on your behalf: */
    whatTheTokenCanDo: string;
  };
  onboarding: {
    /** Will a child use this account? You might want to enable %s. */
    enabledKidModeSuggestion: I18nFormat;
    /** Explore the site and have fun :) */
    exploreTheSiteAndHaveFun: string;
    /** Play opponents from around the world. */
    playOpponentsFromAroundTheWorld: string;
    /** This is your profile page. */
    thisIsYourProfilePage: string;
    /** Verify your account */
    verifyYourAccount: string;
    /** Welcome! */
    welcome: string;
    /** What now? Here are a few suggestions: */
    whatNowSuggestions: string;
  };
  patron: {
    /** Amount */
    amount: string;
    /** I'll add %s to help cover the cost of processing this transaction */
    coverFees: I18nFormat;
    /** Date */
    date: string;
    /** Donate */
    donate: string;
    /** Gift Patron wings */
    giftPatronWingsShort: string;
    /** Patron */
    lichessPatron: string;
    /** Lifetime */
    lifetime: string;
    /** Monthly */
    monthly: string;
    /** One-time */
    onetime: string;
    /** Patron since %s */
    patronSince: I18nFormat;
    /** Thank you for your donation! */
    thankYou: string;
    /** Update */
    update: string;
  };
  perfStat: {
    /** Best rated victories */
    bestRated: string;
    /** Highest rating: %s */
    highestRating: I18nFormat;
    /** Not enough rated games have been played to establish a reliable rating. */
    notEnoughRatedGames: string;
    /** now */
    now: string;
    /** provisional */
    provisional: string;
  };
  preferences: {
    /** Bell notification sound */
    bellNotificationSound: string;
    /** Blindfold */
    blindfold: string;
    /** Board coordinates */
    boardCoordinates: string;
    /** Daily email listing your correspondence games */
    correspondenceEmailNotification: string;
    /** Display */
    display: string;
    /** Except in-game */
    exceptInGame: string;
    /** If you have frequent disconnects, try changing the routing. */
    frequentDisconnectsAdvice: string;
    /** Game behaviour */
    gameBehavior: string;
    /** In casual games only */
    inCasualGamesOnly: string;
    /** In-game only */
    inGameOnly: string;
    /** Input moves with the keyboard */
    inputMovesWithTheKeyboard: string;
    /** Input moves with your voice */
    inputMovesWithVoice: string;
    /** Move confirmation */
    moveConfirmation: string;
    /** Network */
    network: string;
    /** Notifications */
    notifications: string;
    /** Challenges */
    notifyChallenge: string;
    /** New direct message */
    notifyDirectMessage: string;
    /** Correspondence game updates */
    notifyGameEvent: string;
    /** Preferences */
    preferences: string;
    /** Privacy */
    privacy: string;
    /** Say "Good game, well played" upon defeat or draw */
    sayGgWpAfterLosingOrDrawing: string;
    /** Show player flairs */
    showFlairs: string;
    /** Show player ratings */
    showPlayerRatings: string;
    /** Takebacks (with opponent approval) */
    takebacksWithOpponentApproval: string;
    /** Tenths of seconds */
    tenthsOfSeconds: string;
    /** Use CDN routing */
    useCdnRouting: string;
    /** Use direct routing */
    useDirectRouting: string;
    /** When time remaining < 10 seconds */
    whenTimeRemainingLessThanTenSeconds: string;
    /** You are currently using Content Delivery Network (CDN) routing. */
    youAreCurrentlyUsingCdnRouting: string;
    /** You are currently using direct routing. */
    youAreCurrentlyUsingDirectRouting: string;
    /** Your preferences have been saved. */
    yourPreferencesHaveBeenSaved: string;
    /** Zen mode */
    zenMode: string;
  };
  puzzle: {
    /** Easier */
    easier: string;
    /** Example */
    example: string;
    /** incorrect */
    failed: string;
    /** Harder */
    harder: string;
    /** hidden */
    hidden: string;
    /** Puzzle history */
    history: string;
    /** Mates */
    mates: string;
    /** Normal */
    normal: string;
    /** Origin */
    origin: string;
    /** Phases */
    phases: string;
    /** Puzzles */
    puzzles: string;
    /** solved */
    solved: string;
  };
  puzzleTheme: {
    /** Castling */
    castling: string;
    /** Endgame */
    endgame: string;
    /** Equality */
    equality: string;
    /** Fork */
    fork: string;
    /** Long puzzle */
    long: string;
    /** Master games */
    master: string;
    /** Checkmate */
    mate: string;
    /** Middlegame */
    middlegame: string;
    /** Healthy mix */
    mix: string;
    /** Opening */
    opening: string;
    /** Pin */
    pin: string;
    /** Promotion */
    promotion: string;
    /** Short puzzle */
    short: string;
  };
  recap: {
    /** What have you been up to this year? */
    awaitQuestion: string;
    /** %s moves */
    nbMoves: I18nPlural;
    /** Your %s recap is ready! */
    recapReady: I18nFormat;
  };
  report: {
    /** In the meantime, you can block this user: */
    blockUserInMeantime: string;
    /** Cheat */
    cheat: string;
    /** The moderators will review it very soon, and take appropriate action. */
    moderatorsWillReviewSoon: string;
    /** We can process your report faster if you write in English. */
    processReportFasterInEnglish: string;
    /** Please provide at least one link to a game for review. */
    provideOneCheatedGameLink: string;
    /** Read more about TwoStones reports */
    readMoreAboutLichessReports: string;
    /** Sandbagging / Boosting / Match fixing */
    reportBoost: string;
    /** Paste the link to the game(s) and explain what is wrong about this user's behaviour. Don't just say "they cheat", but tell us how you came to this conclusion. */
    reportCheatBoostHelp: string;
    /** Harassment / Bullying / Stalking */
    reportHarass: string;
    /** Hate Speech / Sexism */
    reportHate: string;
    /** Maximum 3000 characters. */
    reportMaximumCharacters: string;
    /** Suicide / Self-Injury */
    reportSelfHarm: string;
    /** Spamming */
    reportSpam: string;
    /** Stalling / Leaving Games */
    reportStall: string;
    /** Explain why this username is offensive. Don't just say "it's offensive/inappropriate," but tell us how you came to this conclusion, especially if the offense is obscure, not in English, in slang, or a historical/cultural reference. */
    reportUsernameHelp: string;
    /** Verbal abuse / Cursing / Trolling */
    reportVerbalAbuse: string;
    /** Violence / Threats */
    reportViolence: string;
    /** Thanks for the report */
    thanksForTheReport: string;
    /** Troll */
    troll: string;
    /** What's the matter? */
    whatIsTheMatter: string;
  };
  search: {
    /** Advanced search */
    advancedSearch: string;
    /** A.I. level */
    aiLevel: string;
    /** Analysis */
    analysis: string;
    /** Colour */
    color: string;
    /** Date */
    date: string;
    /** Descending */
    descending: string;
    /** Evaluation */
    evaluation: string;
    /** From */
    from: string;
    /** %s games found */
    gamesFound: I18nPlural;
    /** Include */
    include: string;
    /** Loser */
    loser: string;
    /** Opponent name */
    opponentName: string;
    /** Result */
    result: string;
    /** Search */
    search: string;
    /** Sort by */
    sortBy: string;
    /** Source */
    source: string;
    /** To */
    to: string;
    /** Winner colour */
    winnerColor: string;
  };
  settings: {
    /** Cancel and keep my account */
    cancelKeepAccount: string;
    /** The username will NOT be available for registration again. */
    cantOpenSimilarAccount: string;
    /** Close account */
    closeAccount: string;
    /** Are you sure you want to close your account? */
    closeAccountAreYouSure: string;
    /** Close forever: make it impossible to reopen */
    closeAccountForeverLabel: string;
    /** Prevent reopening the account later. If you tick this box, even administrators will be unable to reopen your account at your request. */
    closeAccountForeverWarning: string;
    /** close your account */
    closeYourAccount: string;
    /** I understand that deleted accounts aren't recoverable */
    deleteAccountConfirmText: string;
    /** Once you delete your account, it's removed from the site and our administrators won't be able to bring it back for you. */
    deleteAccountWarning: string;
    /** Delete your account */
    deleteYourAccount: string;
    /** Your account is managed, and cannot be closed. */
    managedAccountCannotBeClosed: string;
    /** Settings */
    settings: string;
    /** This account is closed. */
    thisAccountIsClosed: string;
    /** We're sorry to see you go. */
    wereSorryToSeeYouGo: string;
    /** Would you like to %s instead? */
    wouldYouLikeToXInstead: I18nFormat;
  };
  site: {
    /** Abort the game */
    abortTheGame: string;
    /** About */
    about: string;
    /** About bots */
    aboutBotsOnLichess: string;
    /** About %s */
    aboutX: I18nFormat;
    /** Accept */
    accept: string;
    /** Accessibility */
    accessibility: string;
    /** You can login right now as %s. */
    accountCanLogin: I18nFormat;
    /** The account %s is closed. */
    accountClosed: I18nFormat;
    /** You do not need a confirmation email. */
    accountConfirmationEmailNotNeeded: string;
    /** The user %s is successfully confirmed. */
    accountConfirmed: I18nFormat;
    /** The account %s was registered without an email. */
    accountRegisteredWithoutEmail: I18nFormat;
    /** Accuracy */
    accuracy: string;
    /** Active players */
    activePlayers: string;
    /** This is a list of devices and applications that are logged into your account. If you notice any suspicious activity, make sure to <a href='%1$s'>check your recovery email address</a> and <a href='%2$s'>change your password</a>. */
    activeSessionsDescription: I18nFormat;
    /** I agree that I will not create multiple accounts (except for the reasons stated in the %s). */
    agreementMultipleAccounts: I18nFormat;
    /** I agree that I will always be respectful to other players. */
    agreementNice: string;
    /** %1$s level %2$s */
    aiNameLevelAiLevel: I18nFormat;
    /** All information is public and optional. */
    allInformationIsPublicAndOptional: string;
    /** All languages */
    allLanguages: string;
    /** All set! */
    allSet: string;
    /** Always */
    always: string;
    /** Analysis board */
    analysis: string;
    /** Anonymous */
    anonymous: string;
    /** Submit */
    apply: string;
    /** Your account is managed. Ask your teacher about removing kid mode. */
    askYourChessTeacherAboutLiftingKidMode: string;
    /** Automatically proceed to next game after moving */
    automaticallyProceedToNextGameAfterMoving: string;
    /** Auto switch */
    autoSwitch: string;
    /** Background */
    background: string;
    /** Background image */
    backgroundImage: string;
    /** Background image URL: */
    backgroundImageUrl: string;
    /** Beware, the game is rated but has no clock! */
    bewareTheGameIsRatedButHasNoClock: string;
    /** Biography */
    biography: string;
    /** Talk about yourself, your interests, what you like in Go, your favourite players, ... */
    biographyDescription: string;
    /** Black */
    black: string;
    /** Black aborted the game */
    blackAborted: string;
    /** Black accepts takeback */
    blackAcceptsTakeback: string;
    /** Black cancels takeback */
    blackCancelsTakeback: string;
    /** Black declines takeback */
    blackDeclinesTakeback: string;
    /** Black didn't move */
    blackDidntMove: string;
    /** Black is victorious */
    blackIsVictorious: string;
    /** Black left the game */
    blackLeftTheGame: string;
    /** Black offers draw */
    blackOffersDraw: string;
    /** Black to play */
    blackPlays: string;
    /** Black proposes takeback */
    blackProposesTakeback: string;
    /** Black ran out of time */
    blackRanOutOfTime: string;
    /** Black resigned */
    blackResigned: string;
    /** You have used the same password on another site, and that site has been compromised. To ensure the safety of your TwoStones account, we need you to set a new password. Thank you for your understanding. */
    blankedPassword: string;
    /** Blitz */
    blitz: string;
    /** Fast games: 3 to 8 minutes */
    blitzDesc: string;
    /** Block */
    block: string;
    /** Blocked */
    blocked: string;
    /** %s blocks */
    blocks: I18nPlural;
    /** Blog */
    blog: string;
    /** Blunder */
    blunder: string;
    /** Board */
    board: string;
    /** Boards */
    boards: string;
    /** Bookmark this game */
    bookmarkThisGame: string;
    /** Brightness */
    brightness: string;
    /** Notification popups disabled by browser setting */
    browserNotificationsDenied: string;
    /** Bullet */
    bullet: string;
    /** Very fast games: less than 3 minutes */
    bulletDesc: string;
    /** by %s */
    by: I18nFormat;
    /** By registering, you agree to the %s. */
    byRegisteringYouAgreeToBeBoundByOur: I18nFormat;
    /** Cancel */
    cancel: string;
    /** It is not possible to filter by rating because your rating is not stable. */
    cannotFilterByUnstableRating: string;
    /** Please solve the Go captcha. */
    'captcha.fail': string;
    /** Capture */
    capture: string;
    /** Castling rights */
    castling: string;
    /** Casual */
    casual: string;
    /** Challenge a friend */
    challengeAFriend: string;
    /** Challenge %s */
    challengeX: I18nFormat;
    /** Change email */
    changeEmail: string;
    /** Change password */
    changePassword: string;
    /** Change username */
    changeUsername: string;
    /** Change your username. This can only be done once and you are only allowed to change the case of the letters in your username. */
    changeUsernameDescription: string;
    /** Only the case of the letters can change. For example "johndoe" to "JohnDoe". */
    changeUsernameNotSame: string;
    /** Chat */
    chat: string;
    /** Chat room */
    chatRoom: string;
    /** Cheat */
    cheat: string;
    /** Cheat Detected */
    cheatDetected: string;
    /** Check all junk, spam, and other folders */
    checkAllEmailFolders: string;
    /** Checkmate */
    checkmate: string;
    /** If you do not receive a confirmation email, check your Spam or Junk folder. Be sure to indicate messages from TwoStones as safe and "not spam", so you can stay informed of important communications. */
    checkSpamOrJunkFolder: string;
    /** Check your Email */
    checkYourEmail: string;
    /** Classical */
    classical: string;
    /** Classical games: 25 minutes and more */
    classicalDesc: string;
    /** Clear field */
    clearField: string;
    /** [Click to reveal email address] */
    clickToRevealEmailAddress: string;
    /** Clock */
    clock: string;
    /** Close */
    close: string;
    /** Community */
    community: string;
    /** Community bots */
    communityBots: string;
    /** Message */
    composeMessage: string;
    /** Computer */
    computer: string;
    /** Computer analysis available */
    computerAnalysisAvailable: string;
    /** Confirm move */
    confirmMove: string;
    /** Congratulations, you won! */
    congratsYouWon: string;
    /** Contrast */
    contrast: string;
    /** Copy to clipboard */
    copyToClipboard: string;
    /** Correspondence */
    correspondence: string;
    /** Correspondence games: one or several days per move */
    correspondenceDesc: string;
    /** Country or region */
    countryRegion: string;
    /** Create */
    create: string;
    /** Create a game */
    createAGame: string;
    /** Create lobby game */
    createLobbyGame: string;
    /** Crosstable */
    crosstable: string;
    /** Cumulative */
    cumulative: string;
    /** Current games */
    currentGames: string;
    /** Current match score */
    currentMatchScore: string;
    /** Current password */
    currentPassword: string;
    /** Custom */
    custom: string;
    /** Dark */
    dark: string;
    /** Database */
    database: string;
    /** Days per turn */
    daysPerTurn: string;
    /** Decline */
    decline: string;
    /** Defeat */
    defeat: string;
    /** Defeat only */
    defeatOnly: string;
    /** Delete */
    delete: string;
    /** Depth %s */
    depthX: I18nFormat;
    /** Description */
    description: string;
    /** Device theme */
    deviceTheme: string;
    /** Disable blind mode */
    disableBlindMode: string;
    /** Disable Kid mode */
    disableKidMode: string;
    /** Conversations */
    discussions: string;
    /** Done */
    done: string;
    /** Download */
    download: string;
    /** Jigo (draw) */
    draw: string;
    /** Draw and defeat */
    drawAndDefeat: string;
    /** Drawn */
    drawn: string;
    /** Draws */
    draws: string;
    /** Duration */
    duration: string;
    /** Edit */
    edit: string;
    /** Edit profile */
    editProfile: string;
    /** Email */
    email: string;
    /** Email address associated to the account */
    emailAssociatedToaccount: string;
    /** It can take some time to arrive. */
    emailCanTakeSomeTime: string;
    /** Help with email confirmation */
    emailConfirmHelp: string;
    /** Didn't receive your confirmation email after signing up? */
    emailConfirmNotReceived: string;
    /** We will send you an email containing a link to log you in. */
    emailLoginInstructions: string;
    /** Email me a link */
    emailMeALink: string;
    /** We have sent an email to %s. */
    emailSent: I18nFormat;
    /** Do not set an email address suggested by someone else. They can use it to steal your account. */
    emailSuggestion: string;
    /** Enable */
    enable: string;
    /** Enable blind mode */
    enableBlindMode: string;
    /** Enable Kid mode */
    enableKidMode: string;
    /** Endgame */
    endgame: string;
    /** Engine */
    engine: string;
    /** This email address is invalid */
    'error.email': string;
    /** This email address is not acceptable. Please double-check it, and try again. */
    'error.email_acceptable': string;
    /** This is already your email address */
    'error.email_different': string;
    /** Email address invalid or already taken */
    'error.email_unique': string;
    /** Must be at most %s */
    'error.max': I18nFormat;
    /** Must be at most %s characters long */
    'error.maxLength': I18nFormat;
    /** Must be at least %s */
    'error.min': I18nFormat;
    /** Must be at least %s characters long */
    'error.minLength': I18nFormat;
    /** Please don't use your username as your password. */
    'error.namePassword': string;
    /** This field is required */
    'error.required': string;
    /** This password is extremely common and too easy to guess. */
    'error.weakPassword': string;
    /** Fast */
    fast: string;
    /** Favourite opponents */
    favoriteOpponents: string;
    /** Featured bots */
    featuredBots: string;
    /** Filter games */
    filterGames: string;
    /** Finished */
    finished: string;
    /** Flair */
    flair: string;
    /** Flip board */
    flipBoard: string;
    /** Focus chat */
    focusChat: string;
    /** Follow */
    follow: string;
    /** Following */
    following: string;
    /** Follow %s */
    followX: I18nFormat;
    /** Forum */
    forum: string;
    /** Friends */
    friends: string;
    /** Friends online: %1$s */
    friendsOnlineAndPlaying: I18nFormat;
    /** You have a game in progress with %s. */
    gameInProgress: I18nFormat;
    /** Game mode */
    gameMode: string;
    /** Games */
    games: string;
    /** Games played */
    gamesPlayed: string;
    /** Game vs %1$s */
    gameVsX: I18nFormat;
    /** Glicko-2 rating */
    glicko2Rating: string;
    /** To that effect, we must ensure that all players follow good practice. */
    goodPractice: string;
    /** Graph */
    graph: string;
    /** Hang on! */
    hangOn: string;
    /** Help: */
    help: string;
    /** Host */
    host: string;
    /** How to avoid this? */
    howToAvoidThis: string;
    /** Hue */
    hue: string;
    /** Human */
    human: string;
    /** If rating is ± %s */
    ifRatingIsPlusMinusX: I18nFormat;
    /** If registered */
    ifRegistered: string;
    /** If you do not get the email within 5 minutes: */
    ifYouDoNotGetTheEmail: string;
    /** If you don't see the email, check other places it might be, like your junk, spam, social, or other folders. */
    ifYouDoNotSeeTheEmailCheckOtherPlaces: string;
    /** Image opacity */
    imageOpacity: string;
    /** Important */
    important: string;
    /** Inbox */
    inbox: string;
    /** Incorrect password */
    incorrectPassword: string;
    /** Increment */
    increment: string;
    /** Increment in seconds */
    incrementInSeconds: string;
    /** Instructions */
    instructions: string;
    /** Invalid authentication code */
    invalidAuthenticationCode: string;
    /** Invalid username or password */
    invalidUsernameOrPassword: string;
    /** invited you to "%1$s". */
    invitedYouToX: I18nFormat;
    /** Private */
    isPrivate: string;
    /** Join */
    join: string;
    /** Joined %s */
    joinedX: I18nFormat;
    /** Join the game */
    joinTheGame: string;
    /** Keyboard shortcuts */
    keyboardShortcuts: string;
    /** go to start/end */
    keyGoToStartOrEnd: string;
    /** move backward/forward */
    keyMoveBackwardOrForward: string;
    /** Kid mode */
    kidMode: string;
    /** This is about safety. In kid mode, all site communications are disabled. Enable this for your children and school students, to protect them from other internet users. */
    kidModeExplanation: string;
    /** Kid mode is enabled. */
    kidModeIsEnabled: string;
    /** Language */
    language: string;
    /** Active %s */
    lastSeenActive: I18nFormat;
    /** Leaderboard */
    leaderboard: string;
    /** Less */
    less: string;
    /** Let other players challenge you */
    letOtherPlayersChallengeYou: string;
    /** Let other players follow you */
    letOtherPlayersFollowYou: string;
    /** Let other players message you */
    letOtherPlayersMessageYou: string;
    /** Level */
    level: string;
    /** TwoStones is a charity and entirely free/libre open source software. We depend on user donations to support our operating costs, development, and content. Without our generous community, we would not be able to keep the site running. Thank you for joining us! */
    lichessPatronInfo: string;
    /** Lifetime score */
    lifetimeScore: string;
    /** Light */
    light: string;
    /** Like */
    like: string;
    /** Liked */
    liked: string;
    /** List */
    list: string;
    /** List players you have blocked */
    listBlockedPlayers: string;
    /** Lobby */
    lobby: string;
    /** Location */
    location: string;
    /** Log in by email */
    logInByEmail: string;
    /** Sign in to chat */
    loginToChat: string;
    /** Sign out */
    logOut: string;
    /** Losing */
    losing: string;
    /** You lost rating points to someone who violated the Terms of Service */
    lostAgainstTOSViolator: string;
    /** Maximum rating of %s */
    maxRatingX: I18nFormat;
    /** Member since */
    memberSince: string;
    /** Memory */
    memory: string;
    /** mentioned you in "%1$s". */
    mentionedYouInX: I18nFormat;
    /** Menu */
    menu: string;
    /** Message */
    message: string;
    /** Middlegame */
    middlegame: string;
    /** Minimum rating of %s */
    minRatingX: I18nFormat;
    /** Minutes per side */
    minutesPerSide: string;
    /** Mobile */
    mobile: string;
    /** Mode */
    mode: string;
    /** More */
    more: string;
    /** More Go engines created by the community. They are hosted by their creators, and might not always be online. */
    moreChessEnginesCreatedByTheLichessCommunity: string;
    /** Move */
    move: string;
    /** Move list navigation */
    moveListNavigation: string;
    /** Move times */
    moveTimes: string;
    /** Name */
    name: string;
    /** %s bookmarks */
    nbBookmarks: I18nPlural;
    /** %s days */
    nbDays: I18nPlural;
    /** %s draws */
    nbDraws: I18nPlural;
    /** %s friends online */
    nbFriendsOnline: I18nPlural;
    /** %s games */
    nbGames: I18nPlural;
    /** %s games in play */
    nbGamesInPlay: I18nPlural;
    /** %s games with you */
    nbGamesWithYou: I18nPlural;
    /** %s hours */
    nbHours: I18nPlural;
    /** %s losses */
    nbLosses: I18nPlural;
    /** %s minutes */
    nbMinutes: I18nPlural;
    /** %s players */
    nbPlayers: I18nPlural;
    /** %s playing */
    nbPlaying: I18nPlural;
    /** %s rated */
    nbRated: I18nPlural;
    /** %s seconds */
    nbSeconds: I18nPlural;
    /** %s wins */
    nbWins: I18nPlural;
    /** Network lag between you and the server */
    networkLagBetweenYouAndLichess: string;
    /** Never */
    never: string;
    /** Never type your TwoStones password on another site! */
    neverTypeYourPassword: string;
    /** New opponent */
    newOpponent: string;
    /** New password */
    newPassword: string;
    /** New password (again) */
    newPasswordAgain: string;
    /** The new passwords don't match */
    newPasswordsDontMatch: string;
    /** Password strength */
    newPasswordStrength: string;
    /** Next */
    next: string;
    /** No */
    no: string;
    /** No challenges. */
    noChallenges: string;
    /** No games found */
    noGameFound: string;
    /** None */
    none: string;
    /** Offline */
    noNetwork: string;
    /** No note yet */
    noNoteYet: string;
    /** Normal */
    normal: string;
    /** Notes */
    notes: string;
    /** Nothing to see here at the moment. */
    nothingToSeeHere: string;
    /** Notifications */
    notifications: string;
    /** Notifications: %1$s */
    notificationsX: I18nFormat;
    /** offline */
    offline: string;
    /** OK */
    ok: string;
    /** One day */
    oneDay: string;
    /** One URL per line. */
    oneUrlPerLine: string;
    /** online */
    online: string;
    /** Online players */
    onlinePlayers: string;
    /** Only existing conversations */
    onlyExistingConversations: string;
    /** Only friends */
    onlyFriends: string;
    /** Opacity */
    opacity: string;
    /** Opening */
    opening: string;
    /** Openings */
    openings: string;
    /** Opponent */
    opponent: string;
    /** Or let your opponent scan this QR code */
    orLetYourOpponentScanQrCode: string;
    /** Or */
    orSeparator: string;
    /** Other */
    other: string;
    /** Password */
    password: string;
    /** Password reset */
    passwordReset: string;
    /** Do not set a password suggested by someone else. They can use it to steal your account. */
    passwordSuggestion: string;
    /** Pause */
    pause: string;
    /** Performance */
    performance: string;
    /** A permanent link for anyone to challenge you with these exact settings. */
    permanentLinkForAnyoneToChallengeYou: string;
    /** Picture */
    picture: string;
    /** Piece set */
    pieceSet: string;
    /** Play */
    play: string;
    /** Play against computer */
    playAgainstComputer: string;
    /** Player */
    player: string;
    /** Players */
    players: string;
    /** Play every game you start. */
    playEveryGame: string;
    /** Playing right now */
    playingRightNow: string;
    /** We aim to provide a pleasant Go experience for everyone. */
    pleasantChessExperience: string;
    /** Points */
    points: string;
    /** Posts */
    posts: string;
    /** When a potential problem is detected, we display this message. */
    potentialProblem: string;
    /** Practice */
    practice: string;
    /** Preview */
    preview: string;
    /** Privacy */
    privacy: string;
    /** Privacy policy */
    privacyPolicy: string;
    /** Proceed to %s */
    proceedToX: I18nFormat;
    /** We can process your report faster if you write in English. */
    processReportFasterInEnglish: string;
    /** Profile */
    profile: string;
    /** Profile completion: %s */
    profileCompletion: I18nFormat;
    /** Puzzles */
    puzzles: string;
    /** Quick pairing */
    quickPairing: string;
    /** Quote */
    quote: string;
    /** Random side */
    randomColor: string;
    /** Rank */
    rank: string;
    /** Rank is updated every %s minutes */
    rankIsUpdatedEveryNbMinutes: I18nPlural;
    /** Rank: %s */
    rankX: I18nFormat;
    /** Rapid */
    rapid: string;
    /** Rapid games: 8 to 25 minutes */
    rapidDesc: string;
    /** Rated */
    rated: string;
    /** Rating */
    rating: string;
    /** Rating filter */
    ratingFilter: string;
    /** %1$s rating over %2$s games */
    ratingXOverYGames: I18nPlural;
    /** Read about our %s. */
    readAboutOur: I18nFormat;
    /** Read the message */
    readTheMessage: string;
    /** really */
    really: string;
    /** Real name */
    realName: string;
    /** Real time */
    realTime: string;
    /** Reason */
    reason: string;
    /** Reconnecting */
    reconnecting: string;
    /** Wait 5 minutes and refresh your email inbox. */
    refreshInboxAfterFiveMinutes: string;
    /** Refund: %1$s %2$s rating points. */
    refundXpointsTimeControlY: I18nFormat;
    /** Rematch */
    rematch: string;
    /** Rematch offer accepted */
    rematchOfferAccepted: string;
    /** Rematch offer cancelled */
    rematchOfferCanceled: string;
    /** Rematch offer declined */
    rematchOfferDeclined: string;
    /** Rematch offer sent */
    rematchOfferSent: string;
    /** Keep me logged in */
    rememberMe: string;
    /** Reopen */
    reopen: string;
    /** Reopen your account */
    reopenYourAccount: string;
    /** If you closed your account, but have since changed your mind, you get a chance of getting your account back. */
    reopenYourAccountDescription: string;
    /** Replies */
    replies: string;
    /** Reply */
    reply: string;
    /** Report a user */
    reportAUser: string;
    /** Paste the link to the game(s) and explain what is wrong about this user's behaviour. Don't just say "they cheat", but tell us how you came to this conclusion. */
    reportCheatBoostHelp: string;
    /** Explain why this username is offensive. Don't just say "it's offensive/inappropriate," but tell us how you came to this conclusion, especially if the offense is obscure, not in English, in slang, or a historical/cultural reference. */
    reportUsernameHelp: string;
    /** Report %s to moderators */
    reportXToModerators: I18nFormat;
    /** Required. */
    required: string;
    /** Reset */
    reset: string;
    /** Resign */
    resign: string;
    /** Resign lost games (don't let the clock run down). */
    resignLostGames: string;
    /** Resign the game */
    resignTheGame: string;
    /** Resume */
    resume: string;
    /** %1$s vs %2$s */
    resVsX: I18nFormat;
    /** Retry */
    retry: string;
    /** Reusable challenge URL */
    reusableChallengeUrl: string;
    /** revoke all sessions */
    revokeAllSessions: string;
    /** Roundness */
    roundness: string;
    /** Save */
    save: string;
    /** Search */
    search: string;
    /** Search or start new conversation */
    searchOrStartNewDiscussion: string;
    /** Search time */
    searchTime: string;
    /** Security */
    security: string;
    /** Send */
    send: string;
    /** If you can't find your confirmation email, send an email to %s and we'll confirm your account for you. */
    sendEmailForAccountVerification: I18nFormat;
    /** We've sent you an email with a link. */
    sentEmailWithLink: string;
    /** Sessions */
    sessions: string;
    /** Set your flair */
    setFlair: string;
    /** Settings */
    settings: string;
    /** View this help overlay */
    showHelpDialog: string;
    /** Show password */
    showPassword: string;
    /** You have received a private message from the site. */
    showUnreadLichessMessage: string;
    /** Side */
    side: string;
    /** Sign in */
    signIn: string;
    /** Register */
    signUp: string;
    /** We will only send you emails about your account and never share your email. */
    signupEmailPromise: string;
    /** Be sure to choose a username appropriate for all ages. You cannot change it later, and accounts with inappropriate usernames will be closed! */
    signupUsernameHint: string;
    /** Since */
    since: string;
    /** Free online Go server. Play Go (Baduk, Weiqi) in a clean interface. No registration, no ads, no plugin required. Play Go against the computer, friends or random opponents. */
    siteDescription: string;
    /** Size */
    size: string;
    /** Slow */
    slow: string;
    /** Social media links */
    socialMediaLinks: string;
    /** Solution */
    solution: string;
    /** Sorry :( */
    sorry: string;
    /** Sound */
    sound: string;
    /** Source Code */
    sourceCode: string;
    /** Spectator room */
    spectatorRoom: string;
    /** Stalemate */
    stalemate: string;
    /** Standard */
    standard: string;
    /** started streaming */
    startedStreaming: string;
    /** Starting: */
    starting: string;
    /** Stats */
    stats: string;
    /** Stop */
    stop: string;
    /** Strength */
    strength: string;
    /** Subject */
    subject: string;
    /** Subscribe */
    subscribe: string;
    /** Success */
    success: string;
    /** Tags */
    tags: string;
    /** Takeback */
    takeback: string;
    /** Please be nice in the chat! */
    talkInChat: string;
    /** We apologise for the temporary inconvenience, */
    temporaryInconvenience: string;
    /** Terms of Service */
    termsOfService: string;
    /** Thank you! */
    thankYou: string;
    /** Thank you for reading! */
    thankYouForReading: string;
    /** The first person to come to this URL will play with you. */
    theFirstPersonToComeOnThisUrlWillPlayWithYou: string;
    /** Theme */
    theme: string;
    /** The Terms of Service were violated on this account */
    thisAccountViolatedTos: string;
    /** This game is rated */
    thisGameIsRated: string;
    /** Threads */
    threads: string;
    /** Time */
    time: string;
    /** Time is almost up! */
    timeAlmostUp: string;
    /** Time control */
    timeControl: string;
    /** Timeline */
    timeline: string;
    /** Time to process a move on the server */
    timeToProcessAMoveOnLichessServer: string;
    /** Today */
    today: string;
    /** To invite someone to play, give this URL */
    toInviteSomeoneToPlayGiveThisUrl: string;
    /** Tools */
    tools: string;
    /** Topics */
    topics: string;
    /** Tournament */
    tournament: string;
    /** Tournaments */
    tournaments: string;
    /** Time spent playing: %s */
    tpTimeSpentPlaying: I18nFormat;
    /** Troll */
    troll: string;
    /** Try playing these Go engines! They are our favourites. */
    tryPlayingTheseInnovativeChessEngines: string;
    /** Try to win every game you play. */
    tryToWin: string;
    /** Type private notes here */
    typePrivateNotesHere: string;
    /** UltraBullet */
    ultraBullet: string;
    /** Insanely fast games: less than 30 seconds */
    ultraBulletDesc: string;
    /** Unblock */
    unblock: string;
    /** Unfollow */
    unfollow: string;
    /** Unfollow %s */
    unfollowX: I18nFormat;
    /** Unknown */
    unknown: string;
    /** Unlimited */
    unlimited: string;
    /** Take all the time you need */
    unlimitedDescription: string;
    /** Unsubscribe */
    unsubscribe: string;
    /** Until */
    until: string;
    /** User */
    user: string;
    /** Username */
    username: string;
    /** This username is already in use, please try another one. */
    usernameAlreadyUsed: string;
    /** You can use this username to create a new account */
    usernameCanBeUsedForNewAccount: string;
    /** The username must only contain letters, numbers, underscores, and hyphens. Consecutive underscores and hyphens are not allowed. */
    usernameCharsInvalid: string;
    /** We couldn't find any user by this name: %s. */
    usernameNotFound: I18nFormat;
    /** Username or email */
    usernameOrEmail: string;
    /** The username must start with a letter. */
    usernamePrefixInvalid: string;
    /** The username must end with a letter or a number. */
    usernameSuffixInvalid: string;
    /** This username is not acceptable. */
    usernameUnacceptable: string;
    /** Variant */
    variant: string;
    /** Variants */
    variants: string;
    /** Verifying your device... */
    verifyingYourDevice: string;
    /** Verify that %s is your email address */
    verifyYourAddress: I18nFormat;
    /** Victory */
    victory: string;
    /** View in full size */
    viewInFullSize: string;
    /** Views */
    views: string;
    /** Waiting */
    waiting: string;
    /** Waiting for opponent */
    waitingForOpponent: string;
    /** Watch */
    watch: string;
    /** Watch games */
    watchGames: string;
    /** Website */
    website: string;
    /** We had to time you out for a while. */
    weHadToTimeYouOutForAWhile: string;
    /** We've sent you an email. Click the link in the email to activate your account. */
    weHaveSentYouAnEmailClickTheLink: string;
    /** We've sent an email to %s. Click the link in the email to reset your password. */
    weHaveSentYouAnEmailTo: I18nFormat;
    /** What username did you register with? */
    whatSignupUsername: string;
    /** White */
    white: string;
    /** White aborted the game */
    whiteAborted: string;
    /** White accepts takeback */
    whiteAcceptsTakeback: string;
    /** White cancels takeback */
    whiteCancelsTakeback: string;
    /** White declines takeback */
    whiteDeclinesTakeback: string;
    /** White didn't move */
    whiteDidntMove: string;
    /** White is victorious */
    whiteIsVictorious: string;
    /** White left the game */
    whiteLeftTheGame: string;
    /** White offers draw */
    whiteOffersDraw: string;
    /** White to play */
    whitePlays: string;
    /** White proposes takeback */
    whiteProposesTakeback: string;
    /** White ran out of time */
    whiteRanOutOfTime: string;
    /** White resigned */
    whiteResigned: string;
    /** Why? */
    why: string;
    /** Winner */
    winner: string;
    /** Winning */
    winning: string;
    /** Wins */
    wins: string;
    /** and wish you great games on TwoStones. */
    wishYouGreatGames: string;
    /** Withdraw */
    withdraw: string;
    /** Write */
    write: string;
    /** Write a private note about this user */
    writeAPrivateNoteAboutThisUser: string;
    /** %1$s invited you to "%2$s". */
    xInvitedYouToY: I18nFormat;
    /** %1$s mentioned you in "%2$s". */
    xMentionedYouInY: I18nFormat;
    /** %s started streaming */
    xStartedStreaming: I18nFormat;
    /** Yes */
    yes: string;
    /** Yesterday */
    yesterday: string;
    /** You are leaving TwoStones */
    youAreLeavingLichess: string;
    /** You are now part of the team. */
    youAreNowPartOfTeam: string;
    /** You can format text using %s. */
    youCanFormatTextUsing: I18nFormat;
    /** There is a setting to hide all user flairs across the entire site. */
    youCanHideFlair: string;
    /** You can't start a new game while it's your turn. */
    youCantStartNewGame: string;
    /** You have been timed out. */
    youHaveBeenTimedOut: string;
    /** You have joined "%1$s". */
    youHaveJoinedTeamX: I18nFormat;
    /** You need an account to do that */
    youNeedAnAccountToDoThat: string;
    /** Your rating */
    yourRating: string;
    /** Your rating is %s */
    yourRatingIsX: I18nFormat;
    /** Your score: %s */
    yourScore: I18nFormat;
    /** Your turn */
    yourTurn: string;
  };
  storm: {
    /** Accuracy */
    accuracy: string;
    /** Combo */
    combo: string;
    /** Moves */
    moves: string;
    /** Runs */
    runs: string;
    /** Score */
    score: string;
    /** skip */
    skip: string;
    /** Time */
    time: string;
  };
  streamer: {
    /** Connect */
    connect: string;
    /** Disconnect */
    disconnect: string;
    /** Headline */
    headline: string;
    /** LIVE! */
    live: string;
    /** Max size: %s */
    maxSize: I18nFormat;
    /** OFFLINE */
    offline: string;
    /** Streaming rules */
    rules: string;
    /** Your streamer name on Lichess */
    streamerName: string;
    /** Visible on the streamers page */
    visibility: string;
  };
  study: {
    /** Alphabetical */
    alphabetical: string;
    /** Back */
    back: string;
    /** Blunder */
    blunder: string;
    /** Chapter PGN */
    chapterPgn: string;
    /** Contributors */
    contributors: string;
    /** Development */
    development: string;
    /** Editor */
    editor: string;
    /** Empty */
    empty: string;
    /** Everyone */
    everyone: string;
    /** Hot */
    hot: string;
    /** Kick */
    kick: string;
    /** Like */
    like: string;
    /** Members */
    members: string;
    /** %s Games */
    nbGames: I18nPlural;
    /** %s Members */
    nbMembers: I18nPlural;
    /** New tag */
    newTag: string;
    /** Next */
    next: string;
    /** Nobody */
    nobody: string;
    /** Open */
    open: string;
    /** Orientation */
    orientation: string;
    /** Playing */
    playing: string;
    /** Private */
    private: string;
    /** Public */
    public: string;
    /** Relevant */
    relevant: string;
    /** Spectator */
    spectator: string;
    /** Start */
    start: string;
    /** Topics */
    topics: string;
    /** Unlisted */
    unlisted: string;
    /** Visibility */
    visibility: string;
  };
  swiss: {
    /** Comparison */
    comparison: string;
    /** Now playing */
    nowPlaying: string;
    /** Ongoing games */
    ongoingGames: I18nPlural;
    /** Pause */
    pause: string;
    /** Swiss */
    swiss: string;
  };
  team: {
    /** %s members */
    nbMembers: I18nPlural;
    /** Team */
    team: string;
    /** Teams */
    teams: string;
  };
  tfa: {
    /** Authentication code */
    authenticationCode: string;
    /** Disable two-factor authentication */
    disableTwoFactor: string;
    /** Enable two-factor authentication */
    enableTwoFactor: string;
    /** Enter your password and the authentication code generated by the app to complete the setup. You will need an authentication code every time you log in. */
    enterPassword: string;
    /** If you cannot scan the code, enter the secret key %s manually into your app. */
    ifYouCannotScanEnterX: I18nFormat;
    /** Note: If you lose access to your two-factor authentication codes, you can do a %s via email. */
    ifYouLoseAccessTwoFactor: I18nFormat;
    /** Open the two-factor authentication app on your device to view your authentication code and verify your identity. */
    openTwoFactorApp: string;
    /** Scan the QR code with the app. */
    scanTheCode: string;
    /** Please enable two-factor authentication in your account security settings, to secure your account. */
    setupReminder: string;
    /** Get an app for two-factor authentication. We recommend the following apps: */
    twoFactorAppRecommend: string;
    /** Two-factor authentication */
    twoFactorAuth: string;
    /** Two-factor authentication enabled */
    twoFactorEnabled: string;
    /** Two-factor authentication adds another layer of security to your account. */
    twoFactorHelp: string;
    /** You need your password and an authentication code from your authenticator app to disable two-factor authentication. */
    twoFactorToDisable: string;
  };
  timeago: {
    /** completed */
    completed: string;
    /** in %s days */
    inNbDays: I18nPlural;
    /** in %s hours */
    inNbHours: I18nPlural;
    /** in %s minutes */
    inNbMinutes: I18nPlural;
    /** in %s months */
    inNbMonths: I18nPlural;
    /** in %s seconds */
    inNbSeconds: I18nPlural;
    /** in %s weeks */
    inNbWeeks: I18nPlural;
    /** in %s years */
    inNbYears: I18nPlural;
    /** just now */
    justNow: string;
    /** %s days ago */
    nbDaysAgo: I18nPlural;
    /** %s hours ago */
    nbHoursAgo: I18nPlural;
    /** %s hours remaining */
    nbHoursRemaining: I18nPlural;
    /** %s minutes ago */
    nbMinutesAgo: I18nPlural;
    /** %s minutes remaining */
    nbMinutesRemaining: I18nPlural;
    /** %s months ago */
    nbMonthsAgo: I18nPlural;
    /** %s weeks ago */
    nbWeeksAgo: I18nPlural;
    /** %s years ago */
    nbYearsAgo: I18nPlural;
    /** right now */
    rightNow: string;
  };
  ublog: {
    /** All */
    all: string;
    /** Best */
    best: string;
    /** By Lichess */
    byLichess: string;
    /** Community */
    community: string;
    /** Etiquette */
    etiquette: string;
  };
  variant: {
    /** Atomic */
    atomic: string;
    /** Crazyhouse */
    crazyhouse: string;
    /** From Position */
    fromPosition: string;
    /** Racing Kings */
    racingKings: string;
    /** Standard */
    standard: string;
    /** Standard rules of chess (FIDE) */
    standardTitle: string;
  };
  voiceCommands: {
    /** Microphone */
    microphone: string;
    /** Sleep (if wake word enabled) */
    sleep: string;
    /** Voice commands */
    voiceCommands: string;
  };
}
