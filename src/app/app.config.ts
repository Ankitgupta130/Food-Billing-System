import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations'


import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';

import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { TranslateLoader } from '@ngx-translate/core';

import { HttpClient } from '@angular/common/http';
// import { createTranslateLoader } from '../translate.loader';
import { TranslationService } from './services/translation.service';


export function HttpLoaderFactory(http:HttpClient): TranslateLoader{
  return new TranslationService(http);
}

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimations(), provideHttpClient(),
      TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
      provide: TranslateLoader,
      useFactory: HttpLoaderFactory,
      deps: [HttpClient]
      }
    }).providers!
  ]
};
