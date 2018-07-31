import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClimatefinanceSharedModule } from '../../shared';
import {
    IntegrationService,
    IntegrationPopupService,
    IntegrationComponent,
    IntegrationDetailComponent,
    IntegrationDialogComponent,
    IntegrationPopupComponent,
    IntegrationDeletePopupComponent,
    IntegrationDeleteDialogComponent,
    integrationRoute,
    integrationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...integrationRoute,
    ...integrationPopupRoute,
];

@NgModule({
    imports: [
        ClimatefinanceSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        IntegrationComponent,
        IntegrationDetailComponent,
        IntegrationDialogComponent,
        IntegrationDeleteDialogComponent,
        IntegrationPopupComponent,
        IntegrationDeletePopupComponent,
    ],
    entryComponents: [
        IntegrationComponent,
        IntegrationDialogComponent,
        IntegrationPopupComponent,
        IntegrationDeleteDialogComponent,
        IntegrationDeletePopupComponent,
    ],
    providers: [
        IntegrationService,
        IntegrationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceIntegrationModule {}
