import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IDetailedSector } from 'app/shared/model/detailed-sector.model';
import { DetailedSectorService } from './detailed-sector.service';

@Component({
    selector: 'jhi-detailed-sector-update',
    templateUrl: './detailed-sector-update.component.html'
})
export class DetailedSectorUpdateComponent implements OnInit {
    private _detailedSector: IDetailedSector;
    isSaving: boolean;

    constructor(private detailedSectorService: DetailedSectorService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ detailedSector }) => {
            this.detailedSector = detailedSector;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.detailedSector.id !== undefined) {
            this.subscribeToSaveResponse(this.detailedSectorService.update(this.detailedSector));
        } else {
            this.subscribeToSaveResponse(this.detailedSectorService.create(this.detailedSector));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDetailedSector>>) {
        result.subscribe((res: HttpResponse<IDetailedSector>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get detailedSector() {
        return this._detailedSector;
    }

    set detailedSector(detailedSector: IDetailedSector) {
        this._detailedSector = detailedSector;
    }
}
