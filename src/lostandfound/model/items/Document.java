package lostandfound.model.items;

import java.time.LocalDate;
import lostandfound.model.Item;

/**
 * A lost document, such as a passport or student card. Kept for only 7 days
 * because the owner usually needs it back quickly.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Document extends Item {
    private String documentType;
    private String ownerName;


    /**
     * Creates a document.
     *
     * @param id           the unique id of the item
     * @param name         a short name for the item
     * @param description  extra identifying details
     * @param foundBy      who handed it in
     * @param dateFound    the date it was found
     * @param documentType what kind of document it is, for example "Passport"
     * @param ownerName    the name printed on the document
     */
    public Document(int id, String name, String description, String foundBy, LocalDate dateFound,
                    String documentType, String ownerName) {
        super(id, name, description, foundBy, dateFound);
        this.documentType = documentType;
        this.ownerName = ownerName;
    }

    /**
     * Returns what kind of document this is.
     *
     * @return the document type
     */
    public String getDocumentType() {
         return documentType;
    }

    /**
     * Returns the name printed on the document.
     *
     * @return the owner's name
     */
    public String getOwnerName() {
         return ownerName;
    }

    /**
     * Returns the category name.
     *
     * @return {@code "Document"}
     */
    @Override
    public String getCategory() {
         return "Document";
    }


    /**
     * Returns how long documents are kept.
     *
     * @return 7 days
     */
    @Override
    public int getHoldingDays() {
        return 7;
    }

    /**
     * Returns the item summary plus the document type and owner.
     *
     * @return the document as text
     */
    @Override
    public String toString() {
        return super.toString() + " type=" + documentType + ", owner=" + ownerName;
    }

}
