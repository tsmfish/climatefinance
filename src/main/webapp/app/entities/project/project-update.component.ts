import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country';
import { ISector } from 'app/shared/model/sector.model';
import { SectorService } from 'app/entities/sector';
import { IDetailedSector } from 'app/shared/model/detailed-sector.model';
import { DetailedSectorService } from 'app/entities/detailed-sector';

@Component({
    selector: 'jhi-project-update',
    templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit {
    private _project: IProject;
    isSaving: boolean;

    countries: ICountry[];

    sectors: ISector[];

    detailedsectors: IDetailedSector[];
    startYearDp: any;
    endYearDp: any;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private projectService: ProjectService,
        private countryService: CountryService,
        private sectorService: SectorService,
        private detailedSectorService: DetailedSectorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
        });
        this.countryService.query().subscribe(
            (res: HttpResponse<ICountry[]>) => {
                this.countries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.sectorService.query().subscribe(
            (res: HttpResponse<ISector[]>) => {
                this.sectors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.detailedSectorService.query().subscribe(
            (res: HttpResponse<IDetailedSector[]>) => {
                this.detailedsectors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.project.id !== undefined) {
            this.subscribeToSaveResponse(this.projectService.update(this.project));
        } else {
            this.subscribeToSaveResponse(this.projectService.create(this.project));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
        result.subscribe((res: HttpResponse<IProject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCountryById(index: number, item: ICountry) {
        return item.id;
    }

    trackSectorById(index: number, item: ISector) {
        return item.id;
    }

    trackDetailedSectorById(index: number, item: IDetailedSector) {
        return item.id;
    }
    get project() {
        return this._project;
    }

    set project(project: IProject) {
        this._project = project;
    }
}
