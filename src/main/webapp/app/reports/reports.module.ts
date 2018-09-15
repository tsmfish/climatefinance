import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from '../shared';

import { REPORTS_ROUTE, ReportsComponent } from './';

@NgModule({
    imports: [ClimatefinanceSharedModule, RouterModule.forRoot([REPORTS_ROUTE], { useHash: true })],
    declarations: [ReportsComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceAppReportsModule {}
