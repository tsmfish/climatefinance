import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from '../../shared';
import {
    DisbursementService,
    DisbursementPopupService,
    DisbursementComponent,
    DisbursementDetailComponent,
    DisbursementDialogComponent,
    DisbursementPopupComponent,
    DisbursementDeletePopupComponent,
    DisbursementDeleteDialogComponent,
    disbursementRoute,
    disbursementPopupRoute,
} from './';

const ENTITY_STATES = [
    ...disbursementRoute,
    ...disbursementPopupRoute,
];

@NgModule({
    imports: [
        ClimatefinanceSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DisbursementComponent,
        DisbursementDetailComponent,
        DisbursementDialogComponent,
        DisbursementDeleteDialogComponent,
        DisbursementPopupComponent,
        DisbursementDeletePopupComponent,
    ],
    entryComponents: [
        DisbursementComponent,
        DisbursementDialogComponent,
        DisbursementPopupComponent,
        DisbursementDeleteDialogComponent,
        DisbursementDeletePopupComponent,
    ],
    providers: [
        DisbursementService,
        DisbursementPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceDisbursementModule {}
