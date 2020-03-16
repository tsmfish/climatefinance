import { Component, OnInit } from '@angular/core';
import { CountryCount } from '../home/countrycount';
import { GenericCount } from '../home/genericcount';
import { Validcountry } from '../home/validcountry';
import { ChartService } from 'app/charts/charts.service';
import { Router } from '@angular/router';
import { Observable } from '../../../../../node_modules/rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { ValueCount } from 'app/home/valuecount';
import { PdfExportService } from '../shared';

type CombinedCount = {
    name: string;
    number: number;
    total: number;
};

@Component({
    selector: 'jhi-charts',
    templateUrl: './charts.component.html',
    styleUrls: ['charts.css']
})
export class ChartsComponent implements OnInit {
    countryId = '*';

    message: string;

    count: String;
    countryCount: GenericCount[];
    sectorCount: GenericCount[];
    detailedSectorCount: GenericCount[];
    sourceCount: GenericCount[];
    projectTypeCount: GenericCount[];
    projectStatusCount: GenericCount[];
    validCountries: Validcountry[];
    sectorValue: ValueCount[];
    sourceValue: ValueCount[];
    countryValueChart: ValueCount[];
    ministryCount: GenericCount[];

    showLegend = true;
    // colorScheme = { domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA'] };
    colorScheme = 'picnic';

    showLabels = true;
    explodeSlices = false;
    doughnut = false;
    trimLables = false;
    viewBar: any[] = [1400, 700];
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showXAxisLabel = true;
    xAxisLabel = 'Number Of Projects';
    showYAxisLabel = true;
    yAxisLabel = 'Funding Source';

    constructor(private service: ChartService, private router: Router, private pdfService: PdfExportService) {}

    ngOnInit() {
        this.message = 'Under Development';
        this.getValidCountries();
        this.filterCountry('*');
    }

    filterCountry(countryId: any) {
        this.countryId = countryId;

        this.getCount();
        this.getCountryCountChart();
        this.getSectorCount();
        this.getMinistryCount();
        this.getCountryValueChart();
        this.getSectorValue();
        this.getDetailedSectorCount();
        this.getSourceCount();
        this.getSourceValue();
        this.getProjectTypeCount();
        this.getProjectStatusCount();
    }

    private _selectMethod(name: string): Observable<GenericCount[]> {
        const withCountry = this.countryId !== '*';
        const method = `get${name}${withCountry ? 'ByCountry' : ''}`;
        return this.service[method](withCountry ? this.countryId : undefined);
    }

    getCount(): void {
        ((this._selectMethod('Count') as any) as Observable<String>).subscribe(count => (this.count = count));
    }

    getCountryCountChart(): void {
        this._selectMethod('CountryCountChart').subscribe(countryCount => (this.countryCount = countryCount));
    }

    getSectorCount(): void {
        this._selectMethod('SectorCount').subscribe(sectorCount => (this.sectorCount = sectorCount));
    }

    getCountryValueChart(): void {
        this._selectMethod('CountryValueChart').subscribe(countryValueChart => (this.countryValueChart = countryValueChart));
    }

    getMinistryCount(): void {
        this._selectMethod('MinistryCount').subscribe(ministryCount => (this.ministryCount = ministryCount));
    }

    getSectorValue(): void {
        this._selectMethod('SectorValue').subscribe(sectorValue => (this.sectorValue = sectorValue));
    }

    getSourceValue(): void {
        this._selectMethod('SourceValue').subscribe(sourceValue => (this.sourceValue = sourceValue));
    }

    getDetailedSectorCount(): void {
        this._selectMethod('DetailedSectorCount').subscribe(detailedSectorCount => (this.detailedSectorCount = detailedSectorCount));
    }

    getSourceCount(): void {
        this._selectMethod('SourceCount').subscribe(sourceCount => {
            this.sourceCount = sourceCount.map(item => {
                if (!item.name) {
                    return Object.assign({}, item, { name: 'Source not specified' });
                }
                return item;
            });
        });
    }

    getProjectTypeCount(): void {
        this._selectMethod('ProjectTypeCount').subscribe(projectTypeCount => (this.projectTypeCount = projectTypeCount));
    }

    getProjectStatusCount(): void {
        this._selectMethod('ProjectStatusCount').subscribe(projectStatusCount => (this.projectStatusCount = projectStatusCount));
    }

    getValidCountries(): void {
        this.service.getValidCountries().subscribe(validCountries => (this.validCountries = validCountries));
    }

    onSelectChartItem(data, type_) {
        const countryFilter = ` AND country.id:${this.countryId}`;
        const value = data.name || data;
        const params = new URLSearchParams();
        params.append('search', `${type_}:"${value}"`);
        this.router.navigateByUrl(`/project;${params}${countryFilter}`);
    }

    exportPdf(element: HTMLElement) {
        this.pdfService.exportPdf(element);
    }
}
