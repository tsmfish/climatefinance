/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DisbursementUpdateComponent } from 'app/entities/disbursement/disbursement-update.component';
import { DisbursementService } from 'app/entities/disbursement/disbursement.service';
import { Disbursement } from 'app/shared/model/disbursement.model';

describe('Component Tests', () => {
    describe('Disbursement Management Update Component', () => {
        let comp: DisbursementUpdateComponent;
        let fixture: ComponentFixture<DisbursementUpdateComponent>;
        let service: DisbursementService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DisbursementUpdateComponent]
            })
                .overrideTemplate(DisbursementUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DisbursementUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisbursementService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Disbursement(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.disbursement = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Disbursement();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.disbursement = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
