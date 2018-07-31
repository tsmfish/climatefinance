import { NgModule } from '@angular/core';

import { ClimatefinanceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ClimatefinanceSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ClimatefinanceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ClimatefinanceSharedCommonModule {}
