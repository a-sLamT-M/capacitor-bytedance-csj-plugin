import {BytedanceAdPlugin, Config, Result} from "./definitions";
import {WebPlugin} from "@capacitor/core";


export class BytedanceAdWeb extends WebPlugin implements BytedanceAdPlugin {
    constructor() {
        super();
    }

    // @ts-ignore
    init(options: Config): Promise<void> {
        throw this.unimplemented('Not implemented on web.')
    }

    // @ts-ignore
    showSplashAd(options: { slotId: string }): Promise<Result> {
        throw this.unimplemented('Not implemented on web.')
    }
}
