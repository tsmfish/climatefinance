import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IDisbursement } from 'app/shared/model/disbursement.model';
import { DisbursementService } from './disbursement.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
    selector: 'jhi-disbursement-update',
    templateUrl: './disbursement-update.component.html'
})
export class DisbursementUpdateComponent implements OnInit {
    private _disbursement: IDisbursement;
    isSaving: boolean;

    projects: IProject[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private disbursementService: DisbursementService,
        private projectService: ProjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ disbursement }) => {
            this.disbursement = disbursement;
        });
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.disbursement.id !== undefined) {
            this.subscribeToSaveResponse(this.disbursementService.update(this.disbursement));
        } else {
            this.subscribeToSaveResponse(this.disbursementService.create(this.disbursement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDisbursement>>) {
        result.subscribe((res: HttpResponse<IDisbursement>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }
    get disbursement() {
        return this._disbursement;
    }

    set disbursement(disbursement: IDisbursement) {
        this._disbursement = disbursement;
    }
}
