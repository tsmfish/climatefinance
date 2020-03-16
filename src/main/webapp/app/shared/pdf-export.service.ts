import { Injectable } from '@angular/core';

import * as html2canvas from 'html2canvas';
import * as html2pdf from 'html2pdf.js';

import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';

@Injectable()
export class PdfExportService {
    constructor() {
        pdfMake.vfs = pdfFonts.pdfMake.vfs;
    }
    private canvasExport(el: HTMLElement) {
        return html2canvas(el, {
            height: el.clientHeight + 40,
            width: el.clientWidth + 20,
            backgroundColor: null,
            logging: false,
            x: el.getBoundingClientRect().left - 10,
            y: window.scrollY + el.getBoundingClientRect().top,
            onclone: document => {
                el.style.visibility = 'visible';
            }
        }).then(canvas => {
            // Get chart data so we can append to the pdf
            const chartData = canvas.toDataURL();
            // Prepare pdf structure
            const docDefinition = {
                content: [],
                styles: {
                    subheader: {
                        fontSize: 16,
                        bold: true,
                        margin: [0, 10, 0, 5],
                        alignment: 'left'
                    },
                    subsubheader: {
                        fontSize: 12,
                        italics: true,
                        margin: [0, 10, 0, 25],
                        alignment: 'left'
                    }
                },
                defaultStyle: {
                    // alignment: 'justify'
                }
            };

            // Add some content to the pdf
            // const title = { text: 'Here is the export of charts to the PDF', style: 'subheader' };
            // const description = { text: 'Some description', style: 'subsubheader' };
            // docDefinition.content.push(title);
            // docDefinition.content.push(description);
            // Push image of the chart
            docDefinition.content.push({ image: chartData, width: 500 });
            pdfMake.createPdf(docDefinition).download('chartToPdf' + '.pdf');
        });
    }
    private htmlExport(el: HTMLElement) {
        return html2pdf()
            .from(el)
            .save('document.pdf');
    }

    exportPdf(el: HTMLElement, exportType = 'canvas'): Promise<void> {
        switch (exportType) {
            case 'html':
                return this.htmlExport(el);
            case 'canvas':
            default:
                return this.canvasExport(el) as Promise<void>;
        }
    }
}
