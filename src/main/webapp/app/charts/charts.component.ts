import { Component, OnInit } from '@angular/core';
import { CountryCount } from '../home/countrycount';
import { GenericCount } from '../home/genericcount';
import { Validcountry } from '../home/validcountry';
import { ChartService } from 'app/charts/charts.service';
import { Router } from '@angular/router';
import { Observable } from '../../../../../node_modules/rxjs/Observable';
import { catchError } from 'rxjs/operators';
import { ValueCount } from 'app/home/valuecount';

type ProjectTypeTooltip = {
    title: string;
    text: string;
};

const PROJECT_TYPES: { [key: string]: ProjectTypeTooltip } = {
    CCA: {
        title: 'Climate Change Adaptation',
        text:
            'Activities that respond to the adverse impacts of climate change on the environment, human wellbeing and survival, and culture – reducing vulnerability or increasing capacity to make change (resilience). For example coastal defences, food and water security, improving health etc.'
    },
    CCM: {
        title: 'Climate Change Mitigation',
        text:
            'Activities that contribute to lowering the cause of climate change (greenhouse gas emissions). For example, installation of renewable energy sources, fuel efficiency, reducing energy use, carbon storage in vegetation (REDD+), etc.'
    },

    DRM: {
        title: 'Disaster Risk Management',
        text: 'Activities that respond to the damages and losses caused by a disaster on humans, environment and infrastructure'
    },

    DRR: {
        title: 'Disaster Risk Reduction',
        text: 'Activities that contribute to lowering the risks associated with disasters, on humans, environment and infrastructure '
    },

    ENABLING: {
        title: 'Enabling',
        text:
            'This category indicates projects that target institutional and capacity strengthening, creating a more effective enabling environment for the delivery of climate change and disaster risk related activities.'
    }
};

@Component({
    selector: 'jhi-charts',
    templateUrl: './charts.component.html',
    styleUrls: ['charts.css']
})
export class ChartsComponent implements OnInit {
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
    colorScheme = {
        domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
    };
    showLabels = true;
    explodeSlices = false;
    doughnut = false;

    viewBar: any[] = [1400, 700];
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showXAxisLabel = true;
    xAxisLabel = 'Number Of Projects';
    showYAxisLabel = true;
    yAxisLabel = 'Funding Source';

    scheme = 'cool';

    constructor(private service: ChartService, private router: Router) {}

    ngOnInit() {
        this.message = 'Under Development';

        this.getCount();
        this.getCountryCountChart();
        this.getSectorCount();
        this.getSourceCount();
        this.getProjectTypeCount();
        this.getValidCountries();
        this.getDetailedSectorCount();
        this.getProjectStatusCount();
        this.getSectorValue();
        this.getCountryValueChart();
        this.getMinistryCount();
    }

    filterCountry(countryId: any) {
        if (countryId == '*') {
            this.getCount();
            this.getCountryCountChart();
            this.getSectorCount();
            this.getSourceCount();
            this.getProjectTypeCount();
            this.getDetailedSectorCount();
            this.getProjectStatusCount();
            this.getSectorValue();
            this.getCountryValueChart();
            this.getMinistryCount();
        } else {
            this.service.getCountByCountry(countryId).subscribe(count => (this.count = count));
            this.service.getCountryCountChartByCountry(countryId).subscribe(countryCount => (this.countryCount = countryCount));
            this.service.getSectorCountByCountry(countryId).subscribe(sectorCount => (this.sectorCount = sectorCount));
            this.service.getMinistryCountByCountry(countryId).subscribe(ministryCount => (this.ministryCount = ministryCount));
            this.service
                .getDetailedSectorCountByCountry(countryId)
                .subscribe(detailedSectorCount => (this.detailedSectorCount = detailedSectorCount));
            this.service.getSourceCountByCountry(countryId).subscribe(sourceCount => (this.sourceCount = sourceCount));
            this.service.getProjectTypeCountByCountry(countryId).subscribe(projectTypeCount => (this.projectTypeCount = projectTypeCount));
            this.service
                .getProjectStatusCountByCountry(countryId)
                .subscribe(projectStatusCount => (this.projectStatusCount = projectStatusCount));
            this.service.getSectorValueByCountry(countryId).subscribe(sectorValue => (this.sectorValue = sectorValue));
            this.service.getSourceValueByCountry(countryId).subscribe(sourceValue => (this.sourceValue = sourceValue));
            this.service
                .getCountryValueChartByCountry(countryId)
                .subscribe(countryValueChart => (this.countryValueChart = countryValueChart));
        }
    }

    getCount(): void {
        this.service.getCount().subscribe(count => (this.count = count));
    }

    getCountryCountChart(): void {
        this.service.getCountryCountChart().subscribe(countryCount => (this.countryCount = countryCount));
    }

    getCountryValueChart(): void {
        this.service.getCountryValueChart().subscribe(countryValueChart => (this.countryValueChart = countryValueChart));
    }

    getSectorCount(): void {
        this.service.getSectorCount().subscribe(sectorCount => (this.sectorCount = sectorCount));
    }

    getMinistryCount(): void {
        this.service.getMinistryCount().subscribe(ministryCount => (this.ministryCount = ministryCount));
    }

    getSectorValue(): void {
        this.service.getSectorValue().subscribe(sectorValue => (this.sectorValue = sectorValue));
    }

    getSourceValue(): void {
        this.service.getSourceValue().subscribe(sourceValue => (this.sourceValue = sourceValue));
    }

    getDetailedSectorCount(): void {
        this.service.getDetailedSectorCount().subscribe(detailedSectorCount => (this.detailedSectorCount = detailedSectorCount));
    }

    getSourceCount(): void {
        this.service.getSourceCount().subscribe(sourceCount => (this.sourceCount = sourceCount));
    }

    getProjectTypeCount(): void {
        this.service.getProjectTypeCount().subscribe(projectTypeCount => (this.projectTypeCount = projectTypeCount));
    }

    getProjectStatusCount(): void {
        this.service.getProjectStatusCount().subscribe(projectStatusCount => (this.projectStatusCount = projectStatusCount));
    }

    getValidCountries(): void {
        this.service.getValidCountries().subscribe(validCountries => (this.validCountries = validCountries));
    }

    onSelectCountry(data) {
        this.router.navigateByUrl('/project;search=country.name:' + data.name);
    }

    onSelectMinistry(data) {
        this.router.navigateByUrl('/project;search=ministry:' + data.name);
    }

    onSelectSector(data) {
        this.router.navigateByUrl('/project;search=sector.name:' + data.name);
    }

    onSelectDetailedSector(data) {
        this.router.navigateByUrl('/project;search=detailedSector.name:' + data.name);
    }

    onSelectSource(data) {
        this.router.navigateByUrl('/project;search=principalSource:' + data.name);
    }

    onSelectProjectType(data) {
        this.router.navigateByUrl('/project;search=projectType:' + data.name);
    }

    onSelectProjectStatus(data) {
        this.router.navigateByUrl('/project;search=status:' + data.name);
    }

    formatProjectTypeTooltip(name: string) {
        return PROJECT_TYPES[name] || { title: 'Unknown', text: '' };
    }
}
