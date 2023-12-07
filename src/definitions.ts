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

export interface BytedanceAdPlugin extends Plugin {
    init(options: Config): Promise<void>;
    showSplashAd(options: { slotId: string }): Promise<Result>;
}
