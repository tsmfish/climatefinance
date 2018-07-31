import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from '../../shared';
import {
    DetailedSectorService,
    DetailedSectorPopupService,
    DetailedSectorComponent,
    DetailedSectorDetailComponent,
    DetailedSectorDialogComponent,
    DetailedSectorPopupComponent,
    DetailedSectorDeletePopupComponent,
    DetailedSectorDeleteDialogComponent,
    detailedSectorRoute,
    detailedSectorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...detailedSectorRoute,
    ...detailedSectorPopupRoute,
];

@NgModule({
    imports: [
        ClimatefinanceSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DetailedSectorComponent,
        DetailedSectorDetailComponent,
        DetailedSectorDialogComponent,
        DetailedSectorDeleteDialogComponent,
        DetailedSectorPopupComponent,
        DetailedSectorDeletePopupComponent,
    ],
    entryComponents: [
        DetailedSectorComponent,
        DetailedSectorDialogComponent,
        DetailedSectorPopupComponent,
        DetailedSectorDeleteDialogComponent,
        DetailedSectorDeletePopupComponent,
    ],
    providers: [
        DetailedSectorService,
        DetailedSectorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceDetailedSectorModule {}
