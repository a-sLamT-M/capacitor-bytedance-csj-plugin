import { registerPlugin } from '@capacitor/core';

import type { BytedanceAdPlugin } from './definitions';

const BytedanceAd = registerPlugin<BytedanceAdPlugin>('BytedanceAd', {
    web: () => import('./web').then(m => new m.BytedanceAdWeb()),

});

export * from './definitions';
export { BytedanceAd };
