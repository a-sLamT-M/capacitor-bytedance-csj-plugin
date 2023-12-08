import { Plugin } from '@capacitor/core'


export interface Config {
    appId: string;
    appName: string;
    debug: boolean;
}

export interface Result {
    status: string;
    message?: string;
    result_code?: string;
}

export interface EventResult {
    status: string;
    data?: any;
}

export type SplashAdOptions = {
    slotId: string;
}

export type RewardVideoAdOptions = {
    slotId: string;
    extra?: any;
}

export interface BytedanceAdPlugin extends Plugin {
    init(options: Config): Promise<void>;
    showSplashAd(options: SplashAdOptions): Promise<Result>;
    showRewardVideoAd(options: RewardVideoAdOptions): Promise<Result>;
}
