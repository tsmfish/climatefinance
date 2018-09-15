import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IIntegration } from 'app/shared/model/integration.model';
import { IntegrationService } from './integration.service';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country';

@Component({
    selector: 'jhi-integration-update',
    templateUrl: './integration-update.component.html'
})
export class IntegrationUpdateComponent implements OnInit {
    private _integration: IIntegration;
    isSaving: boolean;

    countries: ICountry[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private integrationService: IntegrationService,
        private countryService: CountryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ integration }) => {
            this.integration = integration;
        });
        this.countryService.query().subscribe(
            (res: HttpResponse<ICountry[]>) => {
                this.countries = res.body;
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
        if (this.integration.id !== undefined) {
            this.subscribeToSaveResponse(this.integrationService.update(this.integration));
        } else {
            this.subscribeToSaveResponse(this.integrationService.create(this.integration));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIntegration>>) {
        result.subscribe((res: HttpResponse<IIntegration>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get integration() {
        return this._integration;
    }

    set integration(integration: IIntegration) {
        this._integration = integration;
    }
}
