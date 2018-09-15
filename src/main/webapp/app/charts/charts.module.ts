import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ClimatefinanceSharedModule } from '../shared';
import { CHARTS_ROUTE, ChartsComponent } from './';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ChartService } from 'app/charts/charts.service';

@NgModule({
    imports: [ClimatefinanceSharedModule, RouterModule.forRoot([CHARTS_ROUTE], { useHash: true }), NgxChartsModule],
    declarations: [ChartsComponent],
    entryComponents: [],
    providers: [ChartService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceAppChartsModule {}
