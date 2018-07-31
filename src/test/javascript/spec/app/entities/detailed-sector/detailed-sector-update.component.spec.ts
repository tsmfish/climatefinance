/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DetailedSectorUpdateComponent } from 'app/entities/detailed-sector/detailed-sector-update.component';
import { DetailedSectorService } from 'app/entities/detailed-sector/detailed-sector.service';
import { DetailedSector } from 'app/shared/model/detailed-sector.model';

describe('Component Tests', () => {
    describe('DetailedSector Management Update Component', () => {
        let comp: DetailedSectorUpdateComponent;
        let fixture: ComponentFixture<DetailedSectorUpdateComponent>;
        let service: DetailedSectorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DetailedSectorUpdateComponent]
            })
                .overrideTemplate(DetailedSectorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DetailedSectorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DetailedSectorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DetailedSector(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.detailedSector = entity;
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
                    const entity = new DetailedSector();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.detailedSector = entity;
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
