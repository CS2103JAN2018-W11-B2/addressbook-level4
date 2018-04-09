package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditMovieCommand;
import seedu.address.logic.commands.EditMovieCommand.EditMovieDescriptor;
import seedu.address.model.movie.Duration;
import seedu.address.model.movie.Movie;
import seedu.address.model.movie.MovieName;
import seedu.address.model.movie.Rating;
import seedu.address.model.movie.StartDate;
import seedu.address.model.tag.Tag;
//@@author slothhy-unused
/**
 * This is not used as this is for building the test for EditMovie
 * A utility class to help with building EditMovieDescriptor objects.
 */
public class EditMovieDescriptorBuilder {

    private EditMovieCommand.EditMovieDescriptor descriptor;

    public EditMovieDescriptorBuilder() {
        descriptor = new EditMovieCommand.EditMovieDescriptor();
    }

    public EditMovieDescriptorBuilder(EditMovieDescriptor descriptor) {
        this.descriptor = new EditMovieDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditMovieDescriptor} with fields containing {@code movie}'s details
     */
    public EditMovieDescriptorBuilder(Movie movie) {
        descriptor = new EditMovieDescriptor();
        descriptor.setName(movie.getName());
        descriptor.setDuration(movie.getDuration());
        descriptor.setRating(movie.getRating());
        descriptor.setStartDate(movie.getStartDate());
        descriptor.setTags(movie.getTags());
    }

    /**
     * Sets the {@code MovieName} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withName(String movieName) {
        descriptor.setName(new MovieName(movieName));
        return this;
    }

    /**
     * Sets the {@code Duration} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withDuration(String duration) {
        descriptor.setDuration(new Duration(duration));
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withRating(String rating) {
        descriptor.setRating(new Rating(rating));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withStartDate(String startDate) {
        descriptor.setStartDate(new StartDate(startDate));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditMovieDescriptor}
     * that we are building.
     */
    public EditMovieDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditMovieDescriptor build() {
        return descriptor;
    }
}
