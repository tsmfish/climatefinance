import { NgModule } from '@angular/core';

import { ClimatefinanceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent, PdfExportService } from './';

@NgModule({
    imports: [ClimatefinanceSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ClimatefinanceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent],
    providers: [PdfExportService]
})
export class ClimatefinanceSharedCommonModule {}
