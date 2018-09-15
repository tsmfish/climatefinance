import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from 'app/shared';
import {
    DetailedSectorComponent,
    DetailedSectorDetailComponent,
    DetailedSectorUpdateComponent,
    DetailedSectorDeletePopupComponent,
    DetailedSectorDeleteDialogComponent,
    detailedSectorRoute,
    detailedSectorPopupRoute
} from './';

const ENTITY_STATES = [...detailedSectorRoute, ...detailedSectorPopupRoute];

@NgModule({
    imports: [ClimatefinanceSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DetailedSectorComponent,
        DetailedSectorDetailComponent,
        DetailedSectorUpdateComponent,
        DetailedSectorDeleteDialogComponent,
        DetailedSectorDeletePopupComponent
    ],
    entryComponents: [
        DetailedSectorComponent,
        DetailedSectorUpdateComponent,
        DetailedSectorDeleteDialogComponent,
        DetailedSectorDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceDetailedSectorModule {}
