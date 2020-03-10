import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ClimatefinanceSharedModule } from '../shared';
import { MethodologyComponent, METHODOLOGY_ROUTE } from './';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ChartService } from 'app/charts/charts.service';

@NgModule({
    imports: [ClimatefinanceSharedModule, RouterModule.forRoot([METHODOLOGY_ROUTE], { useHash: true })],
    declarations: [MethodologyComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClimatefinanceAppMethodologyModule {}
