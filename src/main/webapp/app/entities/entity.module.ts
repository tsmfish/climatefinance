import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ClimatefinanceProjectModule } from './project/project.module';
import { ClimatefinanceDisbursementModule } from './disbursement/disbursement.module';
import { ClimatefinanceSectorModule } from './sector/sector.module';
import { ClimatefinanceDetailedSectorModule } from './detailed-sector/detailed-sector.module';
import { ClimatefinanceCountryModule } from './country/country.module';
import { ClimatefinanceIntegrationModule } from './integration/integration.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ClimatefinanceProjectModule,
        ClimatefinanceDisbursementModule,
        ClimatefinanceSectorModule,
        ClimatefinanceDetailedSectorModule,
        ClimatefinanceCountryModule,
        ClimatefinanceIntegrationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceEntityModule {}
