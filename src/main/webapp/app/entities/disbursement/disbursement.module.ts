import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from 'app/shared';
import {
    DisbursementComponent,
    DisbursementDetailComponent,
    DisbursementUpdateComponent,
    DisbursementDeletePopupComponent,
    DisbursementDeleteDialogComponent,
    disbursementRoute,
    disbursementPopupRoute
} from './';

const ENTITY_STATES = [...disbursementRoute, ...disbursementPopupRoute];

@NgModule({
    imports: [ClimatefinanceSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DisbursementComponent,
        DisbursementDetailComponent,
        DisbursementUpdateComponent,
        DisbursementDeleteDialogComponent,
        DisbursementDeletePopupComponent
    ],
    entryComponents: [
        DisbursementComponent,
        DisbursementUpdateComponent,
        DisbursementDeleteDialogComponent,
        DisbursementDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceDisbursementModule {}
