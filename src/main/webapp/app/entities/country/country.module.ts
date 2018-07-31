import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from '../../shared';
import {
    CountryService,
    CountryPopupService,
    CountryComponent,
    CountryDetailComponent,
    CountryDialogComponent,
    CountryPopupComponent,
    CountryDeletePopupComponent,
    CountryDeleteDialogComponent,
    countryRoute,
    countryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...countryRoute,
    ...countryPopupRoute,
];

@NgModule({
    imports: [
        ClimatefinanceSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CountryComponent,
        CountryDetailComponent,
        CountryDialogComponent,
        CountryDeleteDialogComponent,
        CountryPopupComponent,
        CountryDeletePopupComponent,
    ],
    entryComponents: [
        CountryComponent,
        CountryDialogComponent,
        CountryPopupComponent,
        CountryDeleteDialogComponent,
        CountryDeletePopupComponent,
    ],
    providers: [
        CountryService,
        CountryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceCountryModule {}
