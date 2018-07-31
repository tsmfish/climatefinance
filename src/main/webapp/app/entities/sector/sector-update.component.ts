import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISector } from 'app/shared/model/sector.model';
import { SectorService } from './sector.service';

@Component({
    selector: 'jhi-sector-update',
    templateUrl: './sector-update.component.html'
})
export class SectorUpdateComponent implements OnInit {
    private _sector: ISector;
    isSaving: boolean;

    constructor(private sectorService: SectorService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sector }) => {
            this.sector = sector;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sector.id !== undefined) {
            this.subscribeToSaveResponse(this.sectorService.update(this.sector));
        } else {
            this.subscribeToSaveResponse(this.sectorService.create(this.sector));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISector>>) {
        result.subscribe((res: HttpResponse<ISector>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get sector() {
        return this._sector;
    }

    set sector(sector: ISector) {
        this._sector = sector;
    }
}
