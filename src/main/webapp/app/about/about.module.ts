import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from '../shared';

import { ABOUT_ROUTE, AboutComponent } from './';

@NgModule({
    imports: [ClimatefinanceSharedModule, RouterModule.forRoot([ABOUT_ROUTE], { useHash: true })],
    declarations: [AboutComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceAppAboutModule {}
